package lotto.service;

import static lotto.enums.ViewMessageType.INPUT_SEPARATOR;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lotto.domain.Lotto;
import lotto.domain.Lottos;

import lotto.domain.WinningLotto;
import lotto.domain.WinningResult;
import lotto.enums.WinningRankType;
import lotto.utils.LottoNumbersValidator;
import lotto.utils.PaymentValidator;
import lotto.utils.RandomNumberGenerator;

public class LottoServiceImpl implements LottoService {
    private static LottoService instance = new LottoServiceImpl();
    private static final int LOTTO_PRICE = 1000;
    private static final int MIN_LOTTO_NUMBER = 1;
    private static final int MAX_LOTTO_NUMBER = 45;
    private static final int LOTTO_SIZE = 6;
    private LottoServiceImpl() {}

    public static LottoService getInstance() {
        return instance;
    }

    @Override
    public Lottos buyLottos(String paymentInput) throws NumberFormatException {
        PaymentValidator.validateInteger(paymentInput);
        int payment = Integer.parseInt(paymentInput);

        Lottos lottos = new Lottos(payment);

        int buyCount = payment / LOTTO_PRICE;
        for (int cnt=0; cnt < buyCount; cnt++) {
            lottos.addLotto(issueAutoLotto());
        }
        return lottos;
    }

    public Lotto issueAutoLotto() {
        return new Lotto(
                RandomNumberGenerator.makeUniQueRandomNumbers(
                        MIN_LOTTO_NUMBER,
                        MAX_LOTTO_NUMBER,
                        LOTTO_SIZE
                )
        );
    }

    @Override
    public Lotto drawWinningNumbers(String numbersInput) {
        LottoNumbersValidator.validateSeparator(numbersInput);
        return new Lotto(
                Arrays.stream(numbersInput.split(INPUT_SEPARATOR.getMessage()))
                        .map(Integer::parseInt)
                        .toList()
        );
    }

    @Override
    public int drawBonusNumber(String number) {
        LottoNumbersValidator.validateInteger(number);
        return Integer.parseInt(number);
    }

    @Override
    public WinningResult calculateWinning(Lottos lottos, WinningLotto winningLotto) {
        Map<WinningRankType, Integer> rankingCounts = new HashMap<>();
        for (Lotto lotto : lottos.getLottos()) {
            updateRankingCounts(lotto, winningLotto, rankingCounts);
        }
        return new WinningResult(rankingCounts);
    }

    private void updateRankingCounts(Lotto lotto, WinningLotto winningLotto, Map<WinningRankType, Integer> rankingCounts) {
        int winningCount = 0;
        int bonusCount = 0;
        for (Integer number : lotto.getNumbers()) {
            if (winningLotto.isInWinningNumber(number)) {
                winningCount++;
            }
            else if (winningLotto.isSameBonusNumber(number)) {
                bonusCount++;
            }
        }
        WinningRankType rankingType = WinningRankType.selectRankingType(winningCount, bonusCount);
        rankingCounts.put(
                rankingType,
                rankingCounts.getOrDefault(rankingType, 0) + 1
        );
    }
}
