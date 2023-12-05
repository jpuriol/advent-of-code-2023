fun main() {

    fun parseMapPart1(input: List<String>, validKeys: List<Long>) : Pair<LongMap, Int> {

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

    data class AlmanacPart1(
        val seeds: List<Long>,
        val seedToSoil: LongMap,
        val soilToFertilizer: LongMap,
        val fertilizerToWater: LongMap,
        val waterToLight: LongMap,
        val lightToTemperature: LongMap,
        val temperatureToHumidity: LongMap,
        val humidityToLocation: LongMap,
    )


    fun parseAlmanacPart1(input: List<String>): AlmanacPart1 {

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
                        parseMapPart1(input.slice(index+1..<input.size), seeds)
                    seedToSoil = map
                    skipUntilLine = index+linesToSkip
                    println("seed-to-soil done!")
                }
                "soil-to-fertilizer map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart1(input.slice(index+1..<input.size), seedToSoil.values.toList())
                    soilToFertilizer = map
                    skipUntilLine = index+linesToSkip
                    println("soil-to-fertilizer done!")
                }
                "fertilizer-to-water map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart1(input.slice(index+1..<input.size), soilToFertilizer.values.toList())
                    fertilizerToWater = map
                    skipUntilLine = index+linesToSkip
                    println("fertilizer-to-water done!")
                }
                "water-to-light map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart1(input.slice(index+1..<input.size), fertilizerToWater.values.toList())
                    waterToLight = map
                    skipUntilLine = index+linesToSkip
                    println("water-to-light done!")
                }
                "light-to-temperature map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart1(input.slice(index+1..<input.size), waterToLight.values.toList())
                    lightToTemperature = map
                    skipUntilLine = index+linesToSkip
                    println("light-to-temperature done!")
                }
                "temperature-to-humidity map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart1(input.slice(index+1..<input.size), lightToTemperature.values.toList())
                    temperatureToHumidity = map
                    skipUntilLine = index+linesToSkip
                    println("temperature-to-humidity done!")
                }
                "humidity-to-location map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart1(input.slice(index+1..<input.size), temperatureToHumidity.values.toList())
                    humidityToLocation = map
                    skipUntilLine = index+linesToSkip
                    println("humidity-to-location done!")
                }
            }

        }

        return AlmanacPart1(
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

        val almanac = parseAlmanacPart1(input)

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

    data class AlmanacPart2(
        val seeds: List<LongRange>,
        val seedsToSoil: LongRangeMap,
        val soilToFertilizer: LongRangeMap,
        val fertilizerToWater: LongRangeMap,
        val waterToLight: LongRangeMap,
        val lightToTemperature: LongRangeMap,
        val temperatureToHumidity: LongRangeMap,
        val humidityToLocation: LongRangeMap,
    )

    fun parseMapPart2(input: List<String>) : Pair<LongRangeMap, Int> {

        val map = mutableMapOf<LongRange, LongRange>()

        var lines = 0

        for (line in input) {
            if (line.isBlank())
                break

            val numbers = line.split(" ").mapNotNull { it.toLongOrNull() }

            val destinationRangeStart = numbers[0]
            val sourceRangeStart = numbers[1]
            val rangeLength = numbers[2]

            map[sourceRangeStart..<sourceRangeStart+rangeLength] =
                destinationRangeStart..<destinationRangeStart+rangeLength

            lines++
        }

        return Pair(map, lines)
    }

    fun List<Long>.createPairs() =
        indices.filter { it % 2 == 0 }.mapNotNull() { i ->
            if (i + 1 < this.size)
                Pair(this[i], this[i + 1])
            else
                null
        }

    fun parseAlmanacPart2(input: List<String>): AlmanacPart2 {

        val seeds = input[0].split(' ').mapNotNull { it.toLongOrNull() }
            .createPairs()
            .map { (start, rangeLength) -> start..<(start+rangeLength) }

        var seedToSoil: LongRangeMap = emptyMap()
        var soilToFertilizer: LongRangeMap = emptyMap()
        var fertilizerToWater: LongRangeMap = emptyMap()
        var waterToLight: LongRangeMap = emptyMap()
        var lightToTemperature: LongRangeMap = emptyMap()
        var temperatureToHumidity: LongRangeMap = emptyMap()
        var humidityToLocation: LongRangeMap = emptyMap()

        var skipUntilLine = 1
        for (index in input.indices) {
            if (index < skipUntilLine) continue


            when (input[index].trim()) {
                "seed-to-soil map:" -> {
                    val (map, linesToSkip) = parseMapPart2(input.slice(index + 1..<input.size))
                    seedToSoil = map
                    skipUntilLine = index + linesToSkip
                }
                "soil-to-fertilizer map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart2(input.slice(index + 1..<input.size))
                    soilToFertilizer = map
                    skipUntilLine = index + linesToSkip
                }
                "fertilizer-to-water map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart2(input.slice(index + 1..<input.size))
                    fertilizerToWater = map
                    skipUntilLine = index + linesToSkip
                }
                "water-to-light map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart2(input.slice(index + 1..<input.size))
                    waterToLight = map
                    skipUntilLine = index + linesToSkip
                }
                "light-to-temperature map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart2(input.slice(index + 1..<input.size))
                    lightToTemperature = map
                    skipUntilLine = index + linesToSkip
                }
                "temperature-to-humidity map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart2(input.slice(index + 1..<input.size))
                    temperatureToHumidity = map
                    skipUntilLine = index + linesToSkip
                }
                "humidity-to-location map:" -> {
                    val (map, linesToSkip) =
                        parseMapPart2(input.slice(index + 1..<input.size))
                    humidityToLocation = map
                    skipUntilLine = index + linesToSkip
                }
            }
        }

        return AlmanacPart2(
            seeds = seeds,
            seedsToSoil = seedToSoil,
            soilToFertilizer = soilToFertilizer,
            fertilizerToWater = fertilizerToWater,
            waterToLight = waterToLight,
            lightToTemperature = lightToTemperature,
            temperatureToHumidity = temperatureToHumidity,
            humidityToLocation = humidityToLocation,
        )
    }

    fun mapRanges(range1: LongRange, range2: LongRange, value: Long): Long {
        val ratio = (value - range1.first) / (range1.last - range1.first).toDouble()
        return (ratio * (range2.last - range2.first) + range2.first).toLong()
    }

    fun part2(input: List<String>): Long {

        val almanac = parseAlmanacPart2(input)
        println("$almanac")

        val seeds = almanac.seeds.asSequence().flatMap { it.asSequence() }

        val locations = seeds
            .map { seed ->
                val soil = almanac.seedsToSoil.entries.find { it.key.contains(seed) }?.let { entry ->
                    val (sourceRange, destinationRange) = entry
                    mapRanges(sourceRange, destinationRange, seed)
                } ?: seed

                val fertilizer = almanac.soilToFertilizer.entries.find { it.key.contains(soil) }?.let { entry ->
                    val (sourceRange, destinationRange) = entry
                    mapRanges(sourceRange, destinationRange, soil)
                } ?: soil

                val water = almanac.fertilizerToWater.entries.find { it.key.contains(fertilizer) }?.let { entry ->
                    val (sourceRange, destinationRange) = entry
                    mapRanges(sourceRange, destinationRange, fertilizer)
                } ?: fertilizer

                val light = almanac.waterToLight.entries.find { it.key.contains(water) }?.let { entry ->
                    val (sourceRange, destinationRange) = entry
                    mapRanges(sourceRange, destinationRange, water)
                } ?: water

                val temperature = almanac.lightToTemperature.entries.find { it.key.contains(light) }?.let { entry ->
                    val (sourceRange, destinationRange) = entry
                    mapRanges(sourceRange, destinationRange, light)
                } ?: light

                val humidity =
                    almanac.temperatureToHumidity.entries.find { it.key.contains(temperature) }?.let { entry ->
                        val (sourceRange, destinationRange) = entry
                        mapRanges(sourceRange, destinationRange, temperature)
                    } ?: temperature

                val location = almanac.humidityToLocation.entries.find { it.key.contains(humidity) }?.let { entry ->
                    val (sourceRange, destinationRange) = entry
                    mapRanges(sourceRange, destinationRange, humidity)
                } ?: humidity

                location
            }

        return locations.minOrNull() ?: 0
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
     // part1(input).println()
     part2(input).println()
}

typealias LongMap = Map<Long, Long>
typealias LongRangeMap = Map<LongRange, LongRange>
