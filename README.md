# LibreRandonaut
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

LibreRandonaut is an open-source Android app for generating true random coordinates that allow users to explore their surroundings and perform the activity "randonauting". 

It interfaces with the [QRNG at the Australian National University](https://qrng.anu.edu.au/), where it obtains a list of quantum random numbers, converts them to coordinates, and then computes the Gaussian kernel density estimate of those coordinates to find a point with a statistically anomalous density.

If you're unfamiliar with Randonauting, the concepts of Probability Blind-Spots and Quantum Randomness, I recommend reading [fatum_theory.txt](https://github.com/anonyhoney/fatum-en/blob/master/docs/fatum_theory.txt) that came with the original Fatum project bot that inspired Randonautica. If you have no idea what this is about and are completely new to this field, you should read [this article](https://medium.com/swlh/randonauts-how-a-random-number-generator-can-set-you-free-dfc2a2413e15).

# Usage
- Enable GPS in your phone
- Chose the entropy source
- Set the radius
- Press Generate button to start generating an attractor in the defined area around your location
- When generation finishes, press Open to open the attractor's location in a navigation app

# File entropy source
Another interesting aspect is the temporal independence between generating the entropy and applying it in the context of randonauting. You can generate entropy and use it for randonauting at any time later. The results of randonauting should be similar as if you would generate entropy directly, for example by ANU.

When chosing File as entropy source, you need to have a binary file with random bytes on your phone. You could generate this file with some RNG hardware device or download it from [random.org](https://www.random.org/bytes/). You need to pay attention to not use a file twice for generating attractors, since the "Randonauting" effects are not guaranteed then.


# To-do
- Make user interface nice and more user friendly
- ANU became limited to once request per minute. Improve handling with it
- Implement support for other entropy sources, e.g. [random.org](https://www.random.org/bytes/)
- The code contains logic for accessing an homebrew TRNG devices via the USB port. This functionalit has been outcommented from the UI.

# Download

TODO

[<img src="docs/fdroid.png" alt="Get it on F-Droid" height="90">](https://f-droid.org/packages/librerandonaut/)
[<img src="docs/apk.png" alt="Get it on GitHub" height="90">](https://github.com/librerandonaut/librerandonaut/releases)
[<img src="docs/gplay.png" alt="Get it on Google Play" height="90">](https://play.google.com/store/apps/details?id=app.librerandonaut)

# Screenshots

TODO

