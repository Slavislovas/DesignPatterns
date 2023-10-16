package test.unit;


import dod.game.CompassDirection;
import dod.game.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationUnitTests {
    Location location;

    @BeforeEach
    void init(){
        location = new Location(1,1);
    }

    @Test
    void getRow_success(){
        assertEquals(1, location.getRow());
    }

    @Test
    void getColumn_success(){
        assertEquals(1, location.getCol());
    }

    @Test
    void atOffset_success(){
        Location expectedResult = new Location(2,2);
        Location result = location.atOffset(1,1);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_north_range_2_success(){
        Location expectedResult = new Location(1, -1);
        Location result = location.atCompassDirection(CompassDirection.NORTH, 2);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_east_range_2_success(){
        Location expectedResult = new Location(3, 1);
        Location result = location.atCompassDirection(CompassDirection.EAST, 2);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_south_range_2_success(){
        Location expectedResult = new Location(1, 3);
        Location result = location.atCompassDirection(CompassDirection.SOUTH, 2);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_west_range_2_success(){
        Location expectedResult = new Location(-1, 1);
        Location result = location.atCompassDirection(CompassDirection.WEST, 2);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_range_2_fail_invalidCompassDirection(){
        assertThrows(RuntimeException.class, () -> location.atCompassDirection(CompassDirection.NORTH_EAST, 2));
    }

    @Test
    void atCompassDirection_north_success(){
        Location expectedResult = new Location(1,0);
        Location result = location.atCompassDirection(CompassDirection.NORTH);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_east_success(){
        Location expectedResult = new Location(2,1);
        Location result = location.atCompassDirection(CompassDirection.EAST);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_south_success(){
        Location expectedResult = new Location(1,2);
        Location result = location.atCompassDirection(CompassDirection.SOUTH);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_west_success(){
        Location expectedResult = new Location(0,1);
        Location result = location.atCompassDirection(CompassDirection.WEST);
        assertEquals(expectedResult.getCol(), result.getCol());
        assertEquals(expectedResult.getRow(), result.getRow());
    }

    @Test
    void atCompassDirection_fail_invalidDirection(){
        assertThrows(RuntimeException.class, () -> location.atCompassDirection(CompassDirection.NORTH_EAST));
    }
}
