package lotto.domain;

import java.util.Collections;
import java.util.List;
import lotto.utils.LottoNumbersValidator;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        Collections.sort(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        numbers.forEach(LottoNumbersValidator::validateRange);
        LottoNumbersValidator.validateDuplicateNumbers(numbers);
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public boolean isInNumber(int target) {
        return numbers.contains(target);
    }

    @Override
    public String toString() {
        return "Lotto{" +
                "numbers=" + numbers +
                '}';
    }
}
