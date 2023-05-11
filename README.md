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

# Entropy sources
# ANU
See [QRNG at the Australian National University](https://qrng.anu.edu.au/). 

Unfortunately, the API for ANU was limited to one request per minute. For the generation of an attractor within a radius of 1000 meters two requests are needed. Practical tests have shown that a waiting time of 60 seconds is not sufficient. Currently there is a waiting time of 120 seconds, but this could possibly be reduced.

# File
Another interesting aspect is the temporal independence between generating the entropy and applying it in the context of randonauting. You can generate entropy and use it for randonauting at any time later. The results of randonauting should be similar as if you would generate entropy directly, for example by ANU.

If you choose a file as entropy source, you must have a binary file with random bytes on your phone. You can generate this file with a RNG hardware device or download it from [random.org](https://www.random.org/bytes/). You must be careful not to use a file more than once to generate attractors, because then the "randonauting" effects are not guaranteed.

If you provide a file, make sure it contains enough entropy. To generate one attractor per 1000m radius, 100 data points are required. Each data point requires 8 bytes, 1000m radius requires 800 bytes of entropy.

# To-do
- Improvement of the user interface and more usability.
- Improve the handling of ANU API. E.g. show request errors; add a retry button, etc.
- Implement support for other entropy sources, e.g. downloading raw data automatically from [random.org](https://www.random.org/bytes/)
- Code includes logic for accessing a homebuilt TRNG device via the USB port. This feature has been removed from the UI to not confuse the user.

# Download

TODO

[<img src="docs/fdroid.png" alt="Get it on F-Droid" height="90">](https://f-droid.org/packages/librerandonaut/)
[<img src="docs/apk.png" alt="Get it on GitHub" height="90">](https://github.com/librerandonaut/librerandonaut/releases)

# Screenshots

TODO

