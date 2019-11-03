# mp1 Feedback

## Grade: 3.5

| Compilation | Timeout | Duration |
|:-----------:|:-------:|:--------:|
|Yes||6.61|

## Test Results
### ca.ubc.ece.cpen221.mp1.Task3
| Test Status | Count |
| ----------- | ----- |
|errors|0|
|failures|1|
|skipped|0|
|tests|1|
#### Failed Tests
1. `testSimilarityGrouping(Entry)[1] (java.lang.NullPointerException)`
### ca.ubc.ece.cpen221.mp1.Task2
| Test Status | Count |
| ----------- | ----- |
|errors|0|
|failures|1|
|skipped|0|
|tests|2|
#### Failed Tests
1. `testSimilarity(Entry)[1] (org.opentest4j.AssertionFailedError: expected: <1.0> but was: <-1.0>)`
### ca.ubc.ece.cpen221.mp1.Task1
| Test Status | Count |
| ----------- | ----- |
|errors|0|
|failures|3|
|skipped|0|
|tests|11|
#### Failed Tests
1. `testEchoWaves(List)[1] (org.opentest4j.AssertionFailedError: expected: <true> but was: <false>)`
1. `testHighPassFilter(List)[1] (org.opentest4j.AssertionFailedError: expected: <true> but was: <false>)`
1. `testHighAmplitudeFreqComponent(Entry)[1] (org.opentest4j.AssertionFailedError: expected: <300.0> but was: <-1.0>)`

## Test Coverage
### SoundWave
| Metric | Coverage |
| ------ | -------- |
|LINE_MISSED|24|
|BRANCH_MISSED|22|
|BRANCH_COVERED|118|
|LINE_COVERED|240|
### SoundWaveSimilarity
| Metric | Coverage |
| ------ | -------- |
|LINE_MISSED|2|
|BRANCH_MISSED|0|
|BRANCH_COVERED|0|
|LINE_COVERED|0|

## Other Comments
Specs: (0.5/1) Overall decent specs. append(double[], double[]) would need to require the lengths of the two arrays to be the same if not padding. The spec for append(SoundWave) should not describe how it works internally (it describes the usage of length). highAmplitudeFreqComponent() implicitly describes the Nyquist limit by stating that the computed value is that obtained by the DFT. However, the sampling frequency needs to be stated as it has been made private (making it private is incorrect regardless, though). Testing coverage should not be described in specs (see sendToStereoSpeaker()). Specs become broken when making public elements private (marks deducted for this were deducted under General Code Quality, not here).

General Code Quality: (0/1) Using helper methods and classes is good. However, helper methods should be kept private. In contrast, fields and methods that were public should be kept public. Otherwise, the class' spec would be broken. For example, a client may be expecting to call sendToStereoSpeaker() but cannot as it is now private. A similar issue occurs for SAMPLES_PER_SECOND. There is some duplicated code that could be modularized. Also, some methods are rather long and can be broken down.

Code Style + Comments: (1/1) Good and consistent code conventions. Comment on line 575 is helpful. Variables are generally well-named and code is well organized (e.g. loop structures are clear), making it readable.

**minor: Method `highAmplitudeFreqComponent` has a Cognitive Complexity of 15 (exceeds 5 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `336` to `384`
**minor: Method `similarity` has a Cognitive Complexity of 22 (exceeds 5 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `502` to `572`
**minor: Method `highAmplitudeFreqComponent` has 40 lines of code (exceeds 25 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `336` to `384`
**minor: Identical blocks of code found in 2 locations. Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `364` to `371`
**minor: File `SoundWave.java` has 387 lines of code (exceeds 250 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `1` to `604`
**minor: Method `betaZeroChecker` has a Cognitive Complexity of 20 (exceeds 5 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `440` to `489`
**minor: Method `contains` has 28 lines of code (exceeds 25 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `394` to `428`
**minor: Similar blocks of code found in 2 locations. Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `86` to `92`
**minor: Method `betaZeroChecker` has 43 lines of code (exceeds 25 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `440` to `489`
**minor: Identical blocks of code found in 2 locations. Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `311` to `312`
**minor: Method `add` has 36 lines of code (exceeds 25 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `173` to `215`
**major: Method `similarity` has 59 lines of code (exceeds 25 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `502` to `572`
**minor: Similar blocks of code found in 2 locations. Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `100` to `106`
**minor: Identical blocks of code found in 2 locations. Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `313` to `314`
**minor: Method `contains` has a Cognitive Complexity of 15 (exceeds 5 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `394` to `428`
**minor: Method `add` has a Cognitive Complexity of 13 (exceeds 5 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `173` to `215`
**minor: Identical blocks of code found in 2 locations. Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/SoundWave.java`; lines `372` to `379`
**minor: Method `equals` has a Cognitive Complexity of 9 (exceeds 5 allowed). Consider refactoring.**
file: `src/main/java/ca/ubc/ece/cpen221/mp1/utils/Pair.java`; lines `44` to `55`

append(double[], double[]) was made private. And then some tests take too long!
## Checkstyle Results
### `SoundWaveSimilarity.java`
| Line | Column | Message |
| ---- | ------ | ------- |
| 4 | None | `Using the '.*' form of import should be avoided - ca.ubc.ece.cpen221.mp1.utils.*.` |
| 6 | None | `Missing a Javadoc comment.` |
| 21 | None | `Line is longer than 100 characters (found 101).` |
| 22 | None | `Comment matches to-do format 'TODO:'.` |
| 4 | None | `Using the '.*' form of import should be avoided - ca.ubc.ece.cpen221.mp1.utils.*.` |
| 6 | None | `Missing a Javadoc comment.` |
| 21 | None | `Line is longer than 100 characters (found 101).` |
| 22 | None | `Comment matches to-do format 'TODO:'.` |
### `SoundWave.java`
| Line | Column | Message |
| ---- | ------ | ------- |
| 9 | None | `Missing a Javadoc comment.` |
| 173 | None | `Expected an @return tag.` |
| 302 | 52 | `Name 'RC' must match pattern '^([A-Z][0-9]*|[a-z][a-zA-Z0-9]*)$'.` |
| 413 | 62 | `'1000000d' is a magic number.` |
| 413 | 74 | `'1000000d' is a magic number.` |
| 414 | 62 | `'1000000d' is a magic number.` |
| 414 | 74 | `'1000000d' is a magic number.` |
| 479 | 11 | `'if' is not followed by whitespace.` |
| 574 | 5 | `Missing a Javadoc comment.` |
| 590 | 20 | `'0.0000000000001' is a magic number.` |
| 600 | 18 | `'lchannel' hides a field.` |
| 601 | 18 | `'rchannel' hides a field.` |
| 9 | None | `Missing a Javadoc comment.` |
| 173 | None | `Expected an @return tag.` |
| 302 | 52 | `Name 'RC' must match pattern '^([A-Z][0-9]*|[a-z][a-zA-Z0-9]*)$'.` |
| 413 | 62 | `'1000000d' is a magic number.` |
| 413 | 74 | `'1000000d' is a magic number.` |
| 414 | 62 | `'1000000d' is a magic number.` |
| 414 | 74 | `'1000000d' is a magic number.` |
| 479 | 11 | `'if' is not followed by whitespace.` |
| 574 | 5 | `Missing a Javadoc comment.` |
| 590 | 20 | `'0.0000000000001' is a magic number.` |
| 600 | 18 | `'lchannel' hides a field.` |
| 601 | 18 | `'rchannel' hides a field.` |

