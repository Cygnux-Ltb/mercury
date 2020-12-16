/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

public enum BuySell {
    buy(+1), sell(-1);

    public final int direction;

    BuySell(int direction) {
        this.direction = direction;
    }
}
