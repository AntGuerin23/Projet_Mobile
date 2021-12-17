package com.example.projet_mobile.modals

enum class Province(val id : Int, val taxPercentage : Double) {
    QUEBEC(0, 0.05),
    ONTARIO(1, 0.13),
    B_C(2, 0.05),
    ALBERTA(3, 0.05),
    SASKATCHEWAN(4, 0.05),
    MANITOBA(5, 0.05),
    YUKON(6, 0.05),
    NF_L(7, 0.15),
    N_B(8, 0.15),
    N_S(9, 0.15),
    P_E_I(10, 0.15);
}