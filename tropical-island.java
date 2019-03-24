import java.io.*;

public class TropicalIsland {

    public static void main(String[] args) throws IOException {
        Integer[][] island = {{5,3,4,5},{6,2,1,4},{3,1,1,4},{8,5,4,3}};
        System.out.println(getWaterVolume(island)); // 7
    }

    public static int getWaterVolume(Integer[][] island) {
        int i = 0; 
        int j = 0;
        int result = 0;
        int temp = 0;
        int theCell = 0;
        boolean changes_made = true;
        boolean rainResult = true;
        int isleLength = island.length;
        /* Check if the island is too short, too long, etc */
        if (isleLength < 3) return 0;
        if (isleLength > 50) return -1;
        int isleWidth = island[0].length;
        if (isleWidth < 3) return 0;
        if (isleWidth > 50) return -1;
        /* Check if it is not a rectangle */
        for (i = 0; i < isleLength; ++i) {
            if (island[i].length != isleWidth) return 0;
        }
        /* Add the sea: first+last row, first+last column */
        Integer island2[][] = new Integer[isleLength+2][isleWidth+2];
        for (i = 0; i < isleWidth+2; ++i) {
            island2[0][i] = 0;
            island2[isleLength+1][i] = 0;
        }
        for (i = 0; i < isleLength; ++i){
            island2[i+1][0] = 0;
            for (j = 0; j < isleWidth; ++j){
                temp = island[i][j];
                /* Value too big */
                if (temp > 1000) return -1;
                island2[i+1][j+1] = temp;
            }
            island2[i+1][isleWidth+1]=0;
        }
        island = island2;
        island2 = null;
        isleLength += 2;
        isleWidth += 2;
        /* We have the sea now */
        while (changes_made) {
            changes_made = false;
            for (int y = 2; y < isleLength - 2; ++y){
                for (int x = 2; x < isleWidth - 2; ++x) {
                    /* It rains */
                    rainResult = rain(island, y, x);
                    /* Now collect the results or clean up  */
                    for (i = 1; i < isleLength - 1; ++i){
                        for (j = 1; j < isleWidth - 1; ++j){
                            theCell = island[i][j];
                            if(theCell > 0) continue;
                            /* collect water, flip & increment cells */
                            if(rainResult) {
                                ++result;
                                changes_made = true;
                                island[i][j] =-theCell + 1;
                            /* Clean it up */
                            } else {
                                island[i][j] =-theCell;
                            }    
                        }    
                    }
                }
            }
        }
        return result;
    }

    public static boolean rain(Integer[][] island, int y, int x){
        int theCell;
        int cellLt;
        int cellRt;
        int cellDn;
        int cellUp;
        /* step on (x,y) and flip it. */
        theCell = island[y][x];
        island[y][x] = -theCell;
        /* get cells to the left, top, right and down, unsigned */
        cellLt = island[y][x-1]; if (cellLt < 0) cellLt *= -1;
        cellUp = island[y-1][x]; if (cellUp < 0) cellUp *= -1;
        cellRt = island[y][x+1]; if (cellRt < 0) cellRt *= -1;
        cellDn = island[y+1][x]; if (cellDn < 0) cellDn *= -1;
        /* Water flows away: one or more cells nearby are lower */
        if (  cellLt < theCell || cellUp < theCell
           || cellRt < theCell || cellDn < theCell) {
            return false;
        }
        /* cells around are high. water can stay */
        if (  cellLt > theCell && cellUp > theCell
           && cellRt > theCell && cellDn > theCell) {
            return true;
        }
        /* calls itself for equal surrounding cells */
        if (theCell == island[y][x-1] && !rain(island, y, x-1)) return false;
        /* cellRt, cellLt.. would change before they are checked */
        if (theCell == island[y][x+1] && !rain(island, y, x+1)) return false;
        if (theCell == island[y-1][x] && !rain(island, y-1, x)) return false;
        if (theCell == island[y+1][x] && !rain(island, y+1, x)) return false;    
        return true;
    }
}


