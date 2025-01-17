package co.anitrend.arch.extension.util

import co.anitrend.arch.extension.annotation.SupportExperimental
import co.anitrend.arch.extension.util.time.SupportDateTimeUnit
import co.anitrend.arch.extension.util.time.SupportTimeHelper
import co.anitrend.arch.extension.util.time.SupportTimeInstance
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@SupportExperimental
class SupportTimeHelperTest {

    @Test
    fun `has time period in seconds elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 50 seconds
        val targetUnit = 50

        // where the current time targetUnit after inputTime
        val currentTime = inputTime + targetUnit * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_SECONDS
        )

        // assert time period has elapsed
        assertTrue(result.hasElapsed(inputTime))
    }

    @Test
    fun `has time period in seconds not elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 50 seconds
        val targetUnit = 50

        // where the current time targetUnit before inputTime
        val currentTime = inputTime - 1 * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_SECONDS
        )

        // assert time period has not elapsed
        assertFalse(result.hasElapsed(inputTime))
    }

    @Test
    fun `has time period in minutes elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 30 minutes
        val targetUnit = 30

        // where the current time targetUnit after inputTime
        val currentTime = inputTime + targetUnit * 60 * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_MINUTES
        )

        // assert time period has elapsed
        assertTrue(result.hasElapsed(inputTime))
    }

    @Test
    fun `has time period in minutes not elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 30 minutes
        val targetUnit = 30

        // where the current time targetUnit before inputTime
        val currentTime = inputTime - 1 * 60 * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_MINUTES
        )

        // assert time period has not elapsed
        assertFalse(result.hasElapsed(inputTime))
    }

    @Test
    fun `has time period in hours elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 8 hours
        val targetUnit = 8

        // where the current time targetUnit after inputTime
        val currentTime = inputTime + targetUnit * 60 * 60 * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_HOURS
        )

        // assert time period has elapsed
        assertTrue(result.hasElapsed(inputTime))
    }

    @Test
    fun `has time period in hours not elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 8 hours
        val targetUnit = 8

        // where the current time targetUnit before inputTime
        val currentTime = inputTime - 1 * 60 * 60 * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_HOURS
        )

        // assert time period has not elapsed
        assertFalse(result.hasElapsed(inputTime))
    }

    @Test
    fun `has time period in days elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 3 days
        val targetUnit = 3

        // where the current time targetUnit after inputTime
        val currentTime = inputTime + targetUnit * 24 * 60 * 60 * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_DAYS
        )

        // assert time period has elapsed
        assertTrue(result.hasElapsed(inputTime))
    }

    @Test
    fun `has time period in days not elapsed`() {
        // given a certain time
        val inputTime: SupportTimeInstance = 1560501325000

        // with an elapsed period of 3 days
        val targetUnit = 3

        // where the current time targetUnit before inputTime
        val currentTime = inputTime - 1 * 24 * 60 * 60 * 1_000

        val result = SupportTimeHelper(
            supportCurrentUnixTime = currentTime,
            supportTargetTime = targetUnit,
            supportTimeType = SupportDateTimeUnit.TIME_UNIT_DAYS
        )

        // assert time period has not elapsed
        assertFalse(result.hasElapsed(inputTime))
    }
}