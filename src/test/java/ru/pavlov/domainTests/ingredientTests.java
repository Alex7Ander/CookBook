package ru.pavlov.domainTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.pavlov.domain.Ingredient;

public class ingredientTests {
	@Test
	public void calorieShouldBeDoubleTest() {
		Ingredient ingredient = new Ingredient("", "", "", 1.8, 0.5, 0.8);
		double calorie = ingredient.getCalorie();
		assertEquals(14.9, calorie);
	}
}