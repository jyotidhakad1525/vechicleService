package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.builder.MdServiceReminderPrefBuilder;
import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

class MdServiceReminderPrefRepositoryTest extends BaseTest {

    @Autowired
    private MdServiceReminderPrefRepository mdServiceReminderPrefRepository;

    @Autowired
    private MdCommunicationModeRepository mdCommunicationModeRepository;

    @Autowired
    private MdServiceTypeRepository mdServiceTypeRepository;

    /**
     * Test method to generate test data. Not required to run during unit tests..
     */
    //@Test
    @Transactional
    public void testSave() {
        IntStream.range(1, 85).forEach(id -> {
            MdServiceType mdServiceType = mdServiceTypeRepository.findById(id).orElse(null);
            if (mdServiceType != null) {
                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(1).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(10)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.BEFORE).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(2).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(10)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.BEFORE).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(3).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(30)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.BEFORE).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(1).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(30)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.BEFORE).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(3).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(0)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.BEFORE).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(4).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(0)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.BEFORE).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(2).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(5)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.AFTER).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(3).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(5)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.AFTER).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(2).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(10)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.AFTER).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(1).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(10)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.AFTER).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(4).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(1)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.AFTER).build());

                mdServiceReminderPrefRepository.save(MdServiceReminderPrefBuilder.aMdServiceReminderPref()
                        .withCommunicationMode(mdCommunicationModeRepository.findById(1).get())
                        .withTimeFrame(TimeFrame.DAYS)
                        .withDurationValue(1)
                        .withMdServiceType(mdServiceType)
                        .withExpression(Expression.AFTER).build());
            }
        });
    }

}