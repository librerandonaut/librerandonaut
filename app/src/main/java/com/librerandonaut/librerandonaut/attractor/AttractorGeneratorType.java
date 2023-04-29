package com.librerandonaut.librerandonaut.attractor;

public enum AttractorGeneratorType {
    // Attractor generation logic based on old Fatum C# code
    Fatum,
    // Simple kernel density estimation method with gauss kernel
    Kde1,
    // Kernel density estimation method with gauss kernel with help of library kde/bits_kde.jar
    Kde2
}
