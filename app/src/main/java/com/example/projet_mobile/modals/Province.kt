package com.example.projet_mobile.modals

enum class Province(val id : Int, val provinceName : String, val taxPercentage : Double) {
    QUEBEC(0, "Quebec", 0.05),
    ONTARIO(1, "Ontario", 0.13),
    B_C(2, "British Columbia", 0.05),
    ALBERTA(3, "Alberta", 0.05),
    SASKATCHEWAN(4, "Saskatchewan", 0.05),
    MANITOBA(5, "Manitoba", 0.05),
    YUKON(6, "Yukon", 0.05),
    NF_L(7, "Newfoundland and Labrador", 0.15),
    N_B(8, "New Brunswick", 0.15),
    N_S(9, "Nova Scotia", 0.15),
    P_E_I(10, "Prince Edouard Island", 0.15);
}