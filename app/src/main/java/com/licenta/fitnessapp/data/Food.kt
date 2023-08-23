package com.licenta.fitnessapp.data

enum class Food (
    val displayName: String,
    val calories: Float,
    val proteins: Float,
    val lipides: Float,
    val carbohydrates: Float,
    val fibers: Float
) {
    PLEASESELECTFOOD("Please select food", 0f, 0f, 0f, 0f, 0f),
    WATER("Water", 0f, 0f, 0f, 0f, 0f),
    SHAORMA("Shaorma", 238f, 8.2f, 14.4f, 18.9f, 1.7f),
    PIZZA("Pizza", 305f, 27.5f, 4.6f, 63.7f, 55.7f),
    KEBAB("Kebab", 193f, 18f, 13f, 0.5f,0f),
    FALAFEL("Falafel", 253f, 10f, 10f, 27f, 5.4f);

}

fun stringToFood (food: String): Food {
    for (foodIt in Food.values()) {
        if (food == foodIt.displayName) {
            return foodIt
        }
    }

    return Food.PLEASESELECTFOOD
}