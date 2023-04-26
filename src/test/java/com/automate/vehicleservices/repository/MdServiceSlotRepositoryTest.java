package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceSlot;
import com.automate.vehicleservices.entity.builder.MdServiceSlotBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Chandrashekar V
 */
class MdServiceSlotRepositoryTest extends BaseTest {

    @Autowired
    private MdServiceSlotRepository mdServiceSlotRepository;

    @Autowired
    private MdTenantRepository tenantRepository;

    /**
     * Generates service slots for all service types for all days and deletes the same once done.
     */
    //  @Test
    @Transactional
    @Rollback
    void testSaveAll() {

        Random random = new Random();
        List<MdServiceSlot> mdServiceSlots = new ArrayList<>();

        MdServiceSlotBuilder mdServiceSlotBuilder = MdServiceSlotBuilder.aMdServiceSlot()
                .withMdTenant(tenantRepository.findByTenantIdentifier("bhrthyund"));
        Arrays.stream(DayOfWeek.values()).forEach(day ->
        {
            final int startTime = 8;
            final int endTime = 17;
            IntStream.range(startTime, endTime).forEach(hour -> {
                final int rangeMax = 20;
                final int rangeMin = 5;
                final int one = 1;
                final int randomOffset = one;
                final int zero = 0;
                mdServiceSlots
                        .add(mdServiceSlotBuilder.but().withSlotTimeFrom(LocalTime.of(hour, zero))
                                .withSlotTimeTo(LocalTime.of(hour + one,
                                        zero)).withAvailability((short) random
                                        .nextInt(((rangeMax - rangeMin) + randomOffset) + rangeMin))
                                .withDay(day).build());
            });
        });
        Iterable<MdServiceSlot> serviceSlots = mdServiceSlotRepository.saveAll(mdServiceSlots);

        assertNotNull(
                StreamSupport.stream(serviceSlots.spliterator(), false)
                        .findAny());


    }

    @Test
    void testRuntimeGeneticTypeForRepository() {
        ResolvableType resolvableType = ResolvableType.forClass(mdServiceSlotRepository.getClass())
                .as(CrudRepository.class);

        assertEquals(MdServiceSlot.class, resolvableType.getGeneric(0).getRawClass());
        assertEquals(Integer.class, resolvableType.getGeneric(1).getRawClass());

        String i = "10";

        Class<?> rawClass = resolvableType.getGeneric(1).getRawClass();
        if (rawClass == Integer.class)
            System.out.println("Integer" + Integer.parseInt(i));
        else if (rawClass == String.class)
            System.out.println("Stirng" + i);
        else if (rawClass == Long.class)
            System.out.println("Long" + Long.parseLong(i));
        else if (rawClass == Double.class)
            System.out.println("Double" + Double.parseDouble(i));

    }


    @Test
    void test_overlappingSlots() {

        final var slotOverlapExists = mdServiceSlotRepository.
                overlappingSlots(DayOfWeek.MONDAY, 5,
                        LocalTime.of(8, 0), LocalTime.of(9, 0));
    }
}