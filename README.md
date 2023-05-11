# LibreRandonaut
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

LibreRandonaut is an open-source Android app for generating true random coordinates that allow users to explore their surroundings and perform the activity "randonauting". 

It interfaces with the QRNG at the Australian National University, where it obtains a list of quantum random numbers, converts them to coordinates, and then computes the Gaussian kernel density estimate of those coordinates to find a point with a statistically anomalous density.

If you're unfamiliar with Randonauting, the concepts of Probability Blind-Spots and Quantum Randomness, I recommend reading [fatum_theory.txt](https://github.com/anonyhoney/fatum-en/blob/master/docs/fatum_theory.txt) that came with the original Fatum project bot that inspired Randonautica. If you have no idea what this is about and are completely new to this field, you should read [this article](https://medium.com/swlh/randonauts-how-a-random-number-generator-can-set-you-free-dfc2a2413e15).

# Usage
- Enable GPS in your phone
- Chose ANU for the QRNG at the Australian National University
- Set the radius
- Press Generate button to start generating an attractor in the defined area around your location
- When generation finishes, press Open to 

Limitations:
- Ugly user interface.
TODO

## Download

TODO

[<img src="docs/fdroid.png" alt="Get it on F-Droid" height="90">](https://f-droid.org/packages/librerandonaut/)
[<img src="docs/apk.png" alt="Get it on GitHub" height="90">](https://github.com/librerandonaut/librerandonaut/releases)
[<img src="docs/gplay.png" alt="Get it on Google Play" height="90">](https://play.google.com/store/apps/details?id=app.librerandonaut)

## Screenshots
