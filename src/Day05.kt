fun main() {

    fun parseMap(input: List<String>, validKeys: List<Long>) : Pair<LongMap, Int> {

        val map = mutableMapOf<Long, Long>()

        var lines = 0

        for (line in input) {
            if (line.isBlank())
                break

            val numbers = line.split(" ").mapNotNull { it.toLongOrNull() }

            val destinationRangeStart = numbers[0]
            val sourceRangeStart = numbers[1]
            val rangeLength = numbers[2]

            if (validKeys.find { sourceRangeStart <= it && it <= sourceRangeStart+rangeLength } != null) {
                (0..<rangeLength).forEach { i ->
                    val key = sourceRangeStart+i
                    if (key in validKeys)
                        map[key] = destinationRangeStart+i
                }
            }

            lines++
        }

        validKeys.forEach { key -> map.putIfAbsent(key, key) }

        return Pair(map, lines)
    }

    data class Almanac(
        val seeds: List<Long>,
        val seedToSoil: LongMap,
        val soilToFertilizer: LongMap,
        val fertilizerToWater: LongMap,
        val waterToLight: LongMap,
        val lightToTemperature: LongMap,
        val temperatureToHumidity: LongMap,
        val humidityToLocation: LongMap,
    )


    fun parseAlmanac(input: List<String>): Almanac {

        val seeds = input[0].split(' ').mapNotNull { it.toLongOrNull() }

        var seedToSoil: LongMap = emptyMap()
        var soilToFertilizer: LongMap = emptyMap()
        var fertilizerToWater: LongMap = emptyMap()
        var waterToLight: LongMap = emptyMap()
        var lightToTemperature: LongMap = emptyMap()
        var temperatureToHumidity: LongMap = emptyMap()
        var humidityToLocation: LongMap = emptyMap()

        var skipUntilLine = 1
        for (index in input.indices) {
            if (index < skipUntilLine) continue


            when (input[index].trim()) {
                "seed-to-soil map:" -> {
                    val (map, linesToSkip) =
                        parseMap(input.slice(index+1..<input.size), seeds)
                    seedToSoil = map
                    skipUntilLine = index+linesToSkip
                    println("seed-to-soil done!")
                }
                "soil-to-fertilizer map:" -> {
                    val (map, linesToSkip) =
                        parseMap(input.slice(index+1..<input.size), seedToSoil.values.toList())
                    soilToFertilizer = map
                    skipUntilLine = index+linesToSkip
                    println("soil-to-fertilizer done!")
                }
                "fertilizer-to-water map:" -> {
                    val (map, linesToSkip) =
                        parseMap(input.slice(index+1..<input.size), soilToFertilizer.values.toList())
                    fertilizerToWater = map
                    skipUntilLine = index+linesToSkip
                    println("fertilizer-to-water done!")
                }
                "water-to-light map:" -> {
                    val (map, linesToSkip) =
                        parseMap(input.slice(index+1..<input.size), fertilizerToWater.values.toList())
                    waterToLight = map
                    skipUntilLine = index+linesToSkip
                    println("water-to-light done!")
                }
                "light-to-temperature map:" -> {
                    val (map, linesToSkip) =
                        parseMap(input.slice(index+1..<input.size), waterToLight.values.toList())
                    lightToTemperature = map
                    skipUntilLine = index+linesToSkip
                    println("light-to-temperature done!")
                }
                "temperature-to-humidity map:" -> {
                    val (map, linesToSkip) =
                        parseMap(input.slice(index+1..<input.size), lightToTemperature.values.toList())
                    temperatureToHumidity = map
                    skipUntilLine = index+linesToSkip
                    println("temperature-to-humidity done!")
                }
                "humidity-to-location map:" -> {
                    val (map, linesToSkip) =
                        parseMap(input.slice(index+1..<input.size), temperatureToHumidity.values.toList())
                    humidityToLocation = map
                    skipUntilLine = index+linesToSkip
                    println("humidity-to-location done!")
                }
            }

        }

        return Almanac(
            seeds = seeds,
            seedToSoil = seedToSoil,
            soilToFertilizer = soilToFertilizer,
            fertilizerToWater = fertilizerToWater,
            waterToLight = waterToLight,
            lightToTemperature = lightToTemperature,
            temperatureToHumidity = temperatureToHumidity,
            humidityToLocation = humidityToLocation,
        )
    }

    fun part1(input: List<String>): Long {

        println("Parsing...")

        val almanac = parseAlmanac(input)

        println("**finised parsing**")

        val locations = almanac.seeds
            .asSequence()
            .map { seed -> almanac.seedToSoil.getOrDefault(seed, seed) }
            .map { soil -> almanac.soilToFertilizer.getOrDefault(soil, soil) }
            .map { fertilizer -> almanac.fertilizerToWater.getOrDefault(fertilizer, fertilizer) }
            .map { water -> almanac.waterToLight.getOrDefault(water, water) }
            .map { light -> almanac.lightToTemperature.getOrDefault(light, light) }
            .map { temperature -> almanac.temperatureToHumidity.getOrDefault(temperature, temperature) }
            .map { location -> almanac.humidityToLocation.getOrDefault(location, location) }
            .toList()

        return locations.minOrNull() ?: 0
    }


    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()
//    part2(input).println()
}

typealias LongMap = Map<Long, Long>
