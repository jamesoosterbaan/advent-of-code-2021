# Advent of Code 2021

https://adventofcode.com/2021

To compile a days' solution, and then execute it, run the following commands:

```
> export NUMBER=${DAY_TO_EXECUTE}
> kotlinc src/day${NUMBER}.kt -include-runtime -d day${NUMBER}.jar
> java -jar day${NUMBER}.jar "day${NUMBER}.txt"
```
