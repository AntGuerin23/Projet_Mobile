package com.example.projet_mobile.modals

import com.example.projet_mobile.R

enum class Province(val id : Int, val provinceName : Int, val taxPercentage : Double) {
    QUEBEC(0, R.string.quebec, 0.05),
    ONTARIO(1, R.string.ontario, 0.13),
    B_C(2, R.string.bc, 0.05),
    ALBERTA(3, R.string.alberta, 0.05),
    SASKATCHEWAN(4, R.string.saskat, 0.05),
    MANITOBA(5, R.string.manitoba, 0.05),
    YUKON(6, R.string.yukon, 0.05),
    NF_L(7, R.string.nl, 0.15),
    N_B(8, R.string.nb, 0.15),
    N_S(9, R.string.ns, 0.15),
    P_E_I(10, R.string.pei, 0.15);
}