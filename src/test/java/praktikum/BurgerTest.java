package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BurgerTest {

    protected Burger burger;

    @Mock
    protected Bun bun;

    @Mock
    protected Ingredient dinosaur;

    @Mock
    protected Ingredient sausage;

    @Mock
    protected Ingredient chili_sauce;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Настраиваем моки
        when(bun.getName()).thenReturn("Красная булка");
        when(bun.getPrice()).thenReturn(300f);

        when(dinosaur.getName()).thenReturn("Динозавр");
        when(dinosaur.getType()).thenReturn(IngredientType.FILLING);
        when(dinosaur.getPrice()).thenReturn(200f);

        when(sausage.getName()).thenReturn("Колбаса");
        when(sausage.getType()).thenReturn(IngredientType.FILLING);
        when(sausage.getPrice()).thenReturn(300f);

        when(chili_sauce.getName()).thenReturn("Соус чили");
        when(chili_sauce.getType()).thenReturn(IngredientType.SAUCE);
        when(chili_sauce.getPrice()).thenReturn(100f);
        burger = new Burger();
    }

    @Test
    public void testReceiptContainsBunName() {
    burger.setBuns(bun);
    burger.addIngredient(dinosaur);
    String receipt = burger.getReceipt();
    assertTrue("В чеке содержится название булки", receipt.contains("Красная булка"));
}
    @Test
    public void testReceiptContainsIngredientName() {
    burger.setBuns(bun);
    burger.addIngredient(dinosaur);
    String receipt = burger.getReceipt();
    assertTrue("В чеке содержится название ингридиента", receipt.contains("Динозавр"));
}
    @Test
    public void testReceiptContainsPriceLabel() {
        burger.setBuns(bun);
        burger.addIngredient(dinosaur);
        String receipt = burger.getReceipt();
        assertTrue("В чеке содержится метка цены", receipt.contains("Price:"));
    }

    @Test
    public void testReceiptContainsCorrectPriceValueForOneIngredient() {
        burger.setBuns(bun);
        burger.addIngredient(dinosaur);
        String receipt = burger.getReceipt();

        // Проверяем, что цена 800 присутствует в чеке
        assertTrue("В чеке содержится цена 800", receipt.contains("800"));
    }

    // Проверка рецепта
    @Test
    public void testReceiptStructure() {
        burger.setBuns(bun);
        burger.addIngredient(dinosaur);
        String receipt = burger.getReceipt();
        String lineSep = System.lineSeparator();

        // Формируем ожидаемые части чека
        String expectedTopBun = "(==== Красная булка ====)" + lineSep;
        String expectedIngredient = "= filling Динозавр =" + lineSep;
        String expectedBottomBun = "(==== Красная булка ====)" + lineSep;
        String expectedSpacing = lineSep; // Пустая строка перед ценой
        String expectedPriceEndDot = "Price: 800.000000" + lineSep;
        String expectedPriceEndComma = "Price: 800,000000" + lineSep;

        // Собираем ожидаемый чек для обеих локалей
        String expectedReceiptWithDot = expectedTopBun + expectedIngredient + expectedBottomBun + expectedSpacing + expectedPriceEndDot;
        String expectedReceiptWithComma = expectedTopBun + expectedIngredient + expectedBottomBun + expectedSpacing + expectedPriceEndComma;

        // Проверяем, совпадает ли полученный чек с одним из ожидаемых
        boolean matchesDotLocale = receipt.equals(expectedReceiptWithDot);
        boolean matchesCommaLocale = receipt.equals(expectedReceiptWithComma);

        assertTrue("Структура чека должна соответствовать ожидаемой (с точкой или запятой как разделителем)",
                matchesDotLocale || matchesCommaLocale);
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bun);
        assertNotNull("Булочка должна быть установлена", burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(dinosaur);
        assertEquals("Должен быть добавлен один ингредиент", 1, burger.ingredients.size());
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(dinosaur);
        burger.removeIngredient(0);
        assertEquals("Список ингредиентов должен быть пустым после удаления", 0, burger.ingredients.size());
    }

    @Test
    public void testMoveIngredientMovedElement() {
        burger.addIngredient(dinosaur);
        burger.addIngredient(sausage);
        burger.addIngredient(chili_sauce);
        burger.moveIngredient(0, 1);
        assertEquals("Ингредиент dinosaur должен быть перемещен на позицию 1", dinosaur, burger.ingredients.get(1));
    }

    @Test
    public void testMoveIngredientShiftedElement() {
        burger.addIngredient(dinosaur);
        burger.addIngredient(sausage);
        burger.addIngredient(chili_sauce);
        burger.moveIngredient(0, 1);
        assertEquals("Ингредиент sausage должен переместиться на позицию 0", sausage, burger.ingredients.get(0));
    }

    @Test
    public void testMoveIngredientUnchangedElement() {
        burger.addIngredient(dinosaur);
        burger.addIngredient(sausage);
        burger.addIngredient(chili_sauce);
        burger.moveIngredient(0, 1);
        assertEquals("Ингредиент chili_sauce должен остаться на позиции 2", chili_sauce, burger.ingredients.get(2));
    }

    @Test
    public void testBurgerWithBunOnlyPrice() {
        burger.setBuns(bun);
        assertEquals("Цена бургера только с булкой должна быть 2 * price булки", 600f, burger.getPrice(), 0.01);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPriceWithoutBunThrowsException() {
        burger.getPrice();
    }

    @Test(expected = NullPointerException.class)
    public void testGetReceiptWithoutBunThrowsException() {
        burger.getReceipt();
    }

    @Test
    public void testEmptyBurgerReceiptContainsBunName() {
        burger.setBuns(bun);
        String receipt = burger.getReceipt();
        assertTrue("В чеке содержится название булки", receipt.contains("Красная булка"));
    }

    @Test
    public void testEmptyBurgerReceiptContainsPriceLabel() {
        burger.setBuns(bun);
        String receipt = burger.getReceipt();
        assertTrue("В чеке содержится цена", receipt.contains("Price:"));
    }

    @Test
    public void testEmptyBurgerReceiptContainsCorrectPriceValue() {
        burger.setBuns(bun);
        String receipt = burger.getReceipt();
        // Проверяем, что цена 600 присутствует в чеке
        assertTrue("В чеке содержится цена 600", receipt.contains("600"));
    }
}
