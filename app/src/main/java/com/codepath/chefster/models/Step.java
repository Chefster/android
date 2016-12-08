package com.codepath.chefster.models;

import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;

import org.parceler.Parcel;

/**
 * This class represents one step out of multiple that creates a dish
 */
@Parcel
public class Step implements Comparable {
    public enum Status {
        READY (0), ACTIVE (1), DONE (2);

        private int status;

        Status(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    private Integer order;
    private String description;
    private String dishName;
    private String type;
    private Status status;
    private int preRequisite;
    private Integer durationTime;
    boolean isBusyStep;
    @Nullable private String imageUrl;

    public Step() {
    }

    public Integer getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public String getDishName() {
        return dishName;
    }

    public int getPreRequisite() {
        return preRequisite;
    }

    public String getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int compareTo(Object o) {
        Step other = (Step) o;
        if (other.getDishName().equals(dishName)) {
            if (preRequisite == other.order) return 1;
            else if (order == other.preRequisite) return -1;

        }

        if (type.equals(other.getType())) {
            return durationTime.compareTo(other.getDurationTime());
        } else if (type.equals("Prep")) {
            // We're going to give priority to a cooking step that's longer, because usually
            // you can still do other things while doing a cooking step whereas with prep
            // step, you're totally busy (unless the step is 0 time and then we give that priority)
            if (durationTime == 0) return -1;

            if (durationTime < other.durationTime) {
                return 1;
            } else {
                return 0;
            }
        } else if (type.equals("Cook")) {
            if (durationTime > other.durationTime) {
                return -1;
            } else {
                return 0;
            }
        }

        return 0;
    }
}
