package com.plantry.presentation.addfood.rawdata

import com.plantry.data.dto.response.product.ResponseProductIconListDto

object FoodIconList {
    val Bovines = ResponseProductIconListDto.Food(icon = "\uD83E\uDD69", name = "Bovines")
    val Caprae = ResponseProductIconListDto.Food(icon = "\uD83E\uDD69", name = "Caprae")
    val Equines = ResponseProductIconListDto.Food(icon = "\uD83E\uDD69", name = "Equines")
    val Lagomorphs = ResponseProductIconListDto.Food(icon = "\uD83E\uDD69", name = "Lagomorphs")
    val Ovis = ResponseProductIconListDto.Food(icon = "\uD83E\uDD69", name = "Ovis")
    val Poultry = ResponseProductIconListDto.Food(icon = "\uD83C\uDF57", name = "Poultry")
    val Swine = ResponseProductIconListDto.Food(icon = "\uD83E\uDD53", name = "Swine")
    val Venison = ResponseProductIconListDto.Food(icon = "\uD83E\uDD69", name = "Venison")
    val AnimalFoods = listOf(Bovines, Caprae, Equines, Lagomorphs, Ovis, Poultry, Swine, Venison)

    val Amphibians = ResponseProductIconListDto.Food(icon = "\uD83D\uDC1F", name = "Amphibians")
    val Bivalvia = ResponseProductIconListDto.Food(icon = "\uD83E\uDDAA", name = "Bivalvia")
    val Cetaceans = ResponseProductIconListDto.Food(icon = "\uD83D\uDC33", name = "Cetaceans")
    val Crustaceans = ResponseProductIconListDto.Food(icon = "\uD83E\uDD80", name = "Crustaceans")
    val FishFoods = ResponseProductIconListDto.Food(icon = "\uD83D\uDC1F", name = "Fish Products")
    val Fishes = ResponseProductIconListDto.Food(icon = "\uD83D\uDC1F", name = "Fishes")
    val Mollusks = ResponseProductIconListDto.Food(icon = "\uD83D\uDC19", name = "Mollusks")
    val OtherAquatic =
        ResponseProductIconListDto.Food(icon = "\uD83D\uDC1F", name = "Other Aquatic")
    val Pinnipeds = ResponseProductIconListDto.Food(icon = "\uD83E\uDDAD", name = "Pinnipeds")
    val Roe = ResponseProductIconListDto.Food(icon = "\uD83E\uDD5A", name = "Roe")
    val Seaweed = ResponseProductIconListDto.Food(icon = "\uD83E\uDEB8", name = "Seaweed")
    val AquaticFoods = listOf(
        Amphibians, Bivalvia, Cetaceans, Crustaceans,
        FishFoods, Fishes, Mollusks, OtherAquatic,
        Pinnipeds, Roe, Seaweed
    )

    val BabyFoods =
        listOf(ResponseProductIconListDto.Food(icon = "\uD83C\uDF7C", name = "Baby Products"))

    val Baking = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC2", name = "Baking Good")
    val Condiments = ResponseProductIconListDto.Food(icon = "\uD83E\uDD6B", name = "Condiments")
    val Dressings = ResponseProductIconListDto.Food(icon = "\uD83E\uDED7", name = "Dressings")
    val Sauces = ResponseProductIconListDto.Food(icon = "\uD83E\uDD6B", name = "Sauces")
    val Seasonings = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC2", name = "Seasonings")
    val Substitutes = ResponseProductIconListDto.Food(icon = "\uD83E\uDD5B", name = "Substitutes")
    val Sugars = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC2", name = "Sugars")
    val Wrappers = ResponseProductIconListDto.Food(icon = "\uD83E\uDED3", name = "Wrappers")
    val BakingGoods = listOf(
        Baking, Condiments, Dressings, Sauces,
        Seasonings, Substitutes, Sugars, Wrappers,
    )

    val Alcoholic = ResponseProductIconListDto.Food(icon = "\uD83C\uDF79", name = "Alcoholic")
    val BeveragesIcon = ResponseProductIconListDto.Food(icon = "\uD83E\uDD64", name = "Beverages")
    val Spirit = ResponseProductIconListDto.Food(icon = "\uD83C\uDF79", name = "Spirit")
    val Fermented = ResponseProductIconListDto.Food(icon = "\uD83C\uDF7A", name = "Fermented")
    val Fortified = ResponseProductIconListDto.Food(icon = "\uD83C\uDF77", name = "Fortified Wine")
    val Nutrition = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC3", name = "Nutrition")
    val Others = ResponseProductIconListDto.Food(icon = "\uD83E\uDD64", name = "Others")
    val Waters = ResponseProductIconListDto.Food(icon = "\uD83D\uDCA7", name = "Waters")
    val Beverages = listOf(
        Alcoholic, BeveragesIcon, Spirit, Fermented,
        Fortified, Nutrition, Others, Waters,
    )

    val Bread = ResponseProductIconListDto.Food(icon = "\uD83C\uDF5E", name = "Bread")
    val Cereal = ResponseProductIconListDto.Food(icon = "\uD83E\uDD63", name = "Cereal")
    val Cereals = ResponseProductIconListDto.Food(icon = "\uD83C\uDF3E", name = "Cereals")
    val etcC = ResponseProductIconListDto.Food(icon = "\uD83C\uDF3E", name = "etc Cereals")
    val Doughs = ResponseProductIconListDto.Food(icon = "\uD83C\uDF5E", name = "Doughs")
    val FlatB = ResponseProductIconListDto.Food(icon = "\uD83E\uDED3", name = "Flat Bread")
    val SweetB = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC7", name = "Sweet Bread")
    val OthersB = ResponseProductIconListDto.Food(icon = "\uD83C\uDF5E", name = "Others")
    val YeastB = ResponseProductIconListDto.Food(icon = "\uD83E\uDD6F", name = "Yeast Bread")
    val CerealsAndCerealProducts = listOf(
        Bread, Cereal, Cereals, etcC,
        Doughs, FlatB, SweetB, Waters, OthersB, YeastB,
    )

    val CocoaItem = ResponseProductIconListDto.Food(icon = "\uD83C\uDF6B", name = "Cocoa")
    val CocoaFood = ResponseProductIconListDto.Food(icon = "\uD83C\uDF6B", name = "Cocoa Products")
    val Cocoa = listOf(
        CocoaItem, CocoaFood
    )

    val CoffeeItem = ResponseProductIconListDto.Food(icon = "☕", name = "Coffee")
    val CoffeeProduct = ResponseProductIconListDto.Food(icon = "☕", name = "Coffee Products")
    val Coffee = listOf(
        CoffeeItem, CoffeeProduct
    )

    val Candies = ResponseProductIconListDto.Food(icon = "\uD83C\uDF6C", name = "Candies")
    val Dessert = ResponseProductIconListDto.Food(icon = "\uD83C\uDF69", name = "Dessert")
    val FSweet = ResponseProductIconListDto.Food(icon = "\uD83C\uDF67", name = "Frozen Sweet")
    val MSweet = ResponseProductIconListDto.Food(icon = "\uD83C\uDF6E", name = "Milk Sweet")
    val OSweet = ResponseProductIconListDto.Food(icon = "\uD83C\uDF61", name = "Other Sweets")
    val Spreads = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC8", name = "Spreads")
    val Confectioneries = listOf(
        Candies, Dessert, FSweet, MSweet,
        OSweet, Spreads,
    )

    val American = ResponseProductIconListDto.Food(icon = "\uD83C\uDF54", name = "American")
    val Asian = ResponseProductIconListDto.Food(icon = "\uD83C\uDF59", name = "Asian")
    val Berber = ResponseProductIconListDto.Food(icon = "\uD83E\uDD58", name = "Berber")
    val GroundMeat = ResponseProductIconListDto.Food(icon = "\uD83E\uDD53", name = "Ground meat")
    val Jewish = ResponseProductIconListDto.Food(icon = "\uD83E\uDD57", name = "Jewish")
    val LatinAmerican =
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD59", name = "Latin American")
    val Mexican = ResponseProductIconListDto.Food(icon = "\uD83C\uDF2E", name = "Mexican")
    val Levantine = ResponseProductIconListDto.Food(icon = "\uD83C\uDF2F", name = "Levantine")
    val Pasta = ResponseProductIconListDto.Food(icon = "\uD83C\uDF5D", name = "Pasta")
    val Potato = ResponseProductIconListDto.Food(icon = "\uD83E\uDD54", name = "Potato")
    val Sandwich = ResponseProductIconListDto.Food(icon = "\uD83E\uDD6A", name = "Sandwich")
    val TexMex = ResponseProductIconListDto.Food(icon = "\uD83C\uDF2F", name = "Tex-Mex")
    val OthersD = ResponseProductIconListDto.Food(icon = "\uD83E\uDD58", name = "Others")
    val Dishes = listOf(
        American, Asian, Berber, GroundMeat,
        Jewish, LatinAmerican, Mexican, Levantine,
        Pasta, Potato, Sandwich, TexMex,
        OthersD,
    )

    val Egg = listOf(ResponseProductIconListDto.Food(icon = "\uD83E\uDD5A", name = "Eggs"))

    val AnimalF = ResponseProductIconListDto.Food(icon = "\uD83D\uDC10", name = "Animal Fat")
    val VegetableF = ResponseProductIconListDto.Food(icon = "\uD83E\uDD6C", name = "Vegetable Fat")
    val Fats = listOf(
        AnimalF, VegetableF,
    )

    val Berries = ResponseProductIconListDto.Food(icon = "\uD83C\uDF52", name = "Berries")
    val Citrus = ResponseProductIconListDto.Food(icon = "\uD83C\uDF4A", name = "Citrus")
    val Drupes = ResponseProductIconListDto.Food(icon = "\uD83C\uDF51", name = "Drupes")
    val FruitP = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC3", name = "Fruit Products")
    val OtherF = ResponseProductIconListDto.Food(icon = "\uD83E\uDD6D", name = "Other Fruits")
    val Pomes = ResponseProductIconListDto.Food(icon = "\uD83C\uDF4A", name = "Pomes")
    val TropicalF = ResponseProductIconListDto.Food(icon = "\uD83C\uDF47", name = "Tropical Fruits")
    val Fruits = listOf(
        Berries, Citrus, Drupes, FruitP,
        OtherF, Pomes, TropicalF,
    )

    val Gourds = listOf(ResponseProductIconListDto.Food(icon = "\uD83E\uDD52", name = "Gourds"))

    val Herb = ResponseProductIconListDto.Food(icon = "\uD83E\uDDC2", name = "Herb & Spices")
    val HerbsI = ResponseProductIconListDto.Food(icon = "\uD83C\uDF3F", name = "Herbs")
    val Oilseed = ResponseProductIconListDto.Food(icon = "\uD83E\uDD5C", name = "Oilseed Crops")
    val OthersH = ResponseProductIconListDto.Food(icon = "\uD83C\uDF31", name = "Others")
    val Spices = ResponseProductIconListDto.Food(icon = "\uD83C\uDF36️", name = "Spices")
    val Herbs = listOf(Herb, HerbsI, Oilseed, OthersH, Spices)

    val FermentedM = ResponseProductIconListDto.Food(icon = "️\uD83E\uDDC0", name = "Fermented")
    val FermentMilk = ResponseProductIconListDto.Food(icon = "️\uD83E\uDD5B", name = "Ferment Milk")
    val OthersM = ResponseProductIconListDto.Food(icon = "️\uD83E\uDD5B", name = "Others")
    val UnfermentMilk =
        ResponseProductIconListDto.Food(icon = "️\uD83E\uDD5B", name = "Unferment Milk")
    val Milk = listOf(FermentedM, FermentMilk, OthersM, UnfermentMilk,)

    val Nuts = listOf(ResponseProductIconListDto.Food(icon = "\uD83C\uDF30", name = "Nuts"))

    val Beans = ResponseProductIconListDto.Food(icon = "️\uD83E\uDD5C", name = "Beans")
    val Lentils = ResponseProductIconListDto.Food(icon = "️\uD83E\uDD5C", name = "Lentils")
    val Otherpulses = ResponseProductIconListDto.Food(icon = "️\uD83E\uDD5C", name = "Other pulses")
    val Peas = ResponseProductIconListDto.Food(icon = "️\uD83E\uDD5C", name = "Peas")
    val Pulses = listOf(Beans, Lentils, Otherpulses, Peas,)

    val SnackFood =
        listOf(ResponseProductIconListDto.Food(icon = "\uD83C\uDF7F", name = "Snack Products"))

    val SoyFood = listOf(
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF7F", name = "Soy"),
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF7F", name = "Soy Products"),
    )
    val TeasFood = listOf(
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF75", name = "Herbal Teas"),
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF75", name = "Teas"),
    )
    val Vegetables = listOf(
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD6C", name = "Brassicas"),
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD6C", name = "Cabbages"),
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD51", name = "Fruit Veg"),
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF3F", name = "Green Veg"),
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF3F", name = "Leaf Veg"),
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF44", name = "Mushroom"),
        ResponseProductIconListDto.Food(icon = "\uD83E\uDDC5", name = "Onions Veg"),
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD55", name = "Others"),
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD55", name = "Root Veg"),
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF31", name = "Shoot Veg"),
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD66", name = "Stalk Veg"),
        ResponseProductIconListDto.Food(icon = "\uD83C\uDF60", name = "Tubers"),
        ResponseProductIconListDto.Food(icon = "\uD83E\uDD55", name = "Veg products"),
    )

    val Unclassified =
        listOf(ResponseProductIconListDto.Food(icon = "\uD83C\uDF7D️", name = "Unclassified"))

    val Empty = listOf(
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
        ResponseProductIconListDto.Food(icon = "", name = ""),
    )
    val AnimalFoodList =
        ResponseProductIconListDto(groupName = "Animal Products", foodList = AnimalFoods)
    val AquaticFoodsList =
        ResponseProductIconListDto(groupName = "Aquatic Products", foodList = AquaticFoods)
    val BabyFoodsList =
        ResponseProductIconListDto(groupName = "Baby Products", foodList = BabyFoods)
    val BakingGoodsList =
        ResponseProductIconListDto(groupName = "Baking Goods", foodList = BakingGoods)
    val BeveragesList = ResponseProductIconListDto(groupName = "Beverages", foodList = Beverages)
    val CerealsAndCerealProductsList = ResponseProductIconListDto(
        groupName = "Cereals and cereal products",
        foodList = CerealsAndCerealProducts
    )
    val CocoaLists =
        ResponseProductIconListDto(groupName = "Cocoa and cocoa products", foodList = Cocoa)
    val CoffeeList =
        ResponseProductIconListDto(groupName = "Coffee and coffee products", foodList = Coffee)
    val ConfectioneriesList =
        ResponseProductIconListDto(groupName = "Confectioneries", foodList = Confectioneries)
    val DishesList = ResponseProductIconListDto(groupName = "Dishes", foodList = Dishes)
    val EggList = ResponseProductIconListDto(groupName = "Egg", foodList = Egg)
    val FatsList = ResponseProductIconListDto(groupName = "Fats and Oils", foodList = Fats)
    val FruitsList = ResponseProductIconListDto(groupName = "Fruits", foodList = Fruits)
    val GourdsList = ResponseProductIconListDto(groupName = "Gourds", foodList = Gourds)
    val HerbsList = ResponseProductIconListDto(groupName = "Herbs and Spices", foodList = Herbs)
    val MilkList = ResponseProductIconListDto(groupName = "Nuts", foodList = Milk)
    val NutsList = ResponseProductIconListDto(groupName = "Egg", foodList = Nuts)
    val PulsesList = ResponseProductIconListDto(groupName = "Pulses", foodList = Pulses)
    val SnackFoodList =
        ResponseProductIconListDto(groupName = "Snack Products", foodList = SnackFood)
    val SoyList = ResponseProductIconListDto(groupName = "Soy", foodList = SoyFood)
    val TeasFoodList = ResponseProductIconListDto(groupName = "Teas", foodList = TeasFood)
    val VegetablesList = ResponseProductIconListDto(groupName = "Vegetables", foodList = Vegetables)
    val UnclassifiedList =
        ResponseProductIconListDto(groupName = "Unclassified", foodList = Unclassified)
    val EmptyList = ResponseProductIconListDto(groupName = "", foodList = Empty)


    val foodLists = listOf(
        AnimalFoodList,
        AquaticFoodsList,
        BabyFoodsList,
        BakingGoodsList,
        BeveragesList,
        CerealsAndCerealProductsList,
        CocoaLists,
        CoffeeList,
        ConfectioneriesList,
        DishesList,
        EggList,
        FatsList,
        FruitsList,
        GourdsList,
        HerbsList,
        MilkList,
        NutsList,
        PulsesList,
        SnackFoodList,
        SoyList,
        TeasFoodList,
        VegetablesList,
        UnclassifiedList,
        EmptyList,
    )
}