package com.automate.vehicleservices.framework.common;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class DateHelper {

    public long differenceInDays(final LocalDate from, final LocalDate to) {
        return Math.abs(ChronoUnit.DAYS.between(from, to));
    }


    public long differenceInDays(final LocalDateTime from, final LocalDateTime to) {
        return Math.abs(ChronoUnit.DAYS.between(from, to));
    }

    public long differenceInDaysFromAndNowInclusive(final LocalDate from) {
        return Math.abs(ChronoUnit.DAYS.between(from, LocalDate.now().plusDays(1)));
    }

    public LocalDate addDays(@NotNull LocalDate purchaseDate, int durationDaysTo) {
        return purchaseDate.plusDays(durationDaysTo);
    }

    public LocalDate addMonths(@NotNull LocalDate purchaseDate, int months) {
        return purchaseDate.plusMonths(months);
    }

    public LocalDate addYears(@NotNull LocalDate purchaseDate, int years) {
        return purchaseDate.plusYears(years);
    }
}
