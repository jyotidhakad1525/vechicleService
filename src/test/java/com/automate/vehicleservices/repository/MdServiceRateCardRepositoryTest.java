package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.builder.MdServiceRateCardBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

/**
 * @author Chandrashekar V
 */
class MdServiceRateCardRepositoryTest extends BaseTest {


    public static final String NEW_SANTRO_PETROL = "New Santro - Petrol";
    public static final String TENANT_BHRTHYUND = "bhrthyund";
    public static final double ZERO = 0.0;
    public static final double ZERO_DOUBLE = 0.0;
    public static final int FIVE_HUNDRED = 500;
    public static final double NINE_HUNDRED = 900.0;
    public static final int ONE_HUNDRED_SIXTY = 160;
    public static final int ONE_HUNDRED_EIGHTY = 180;
    public static final int TWO_SEVENTY_FIVE = 275;
    public static final int FOUR_FIFTY = 450;
    public static final int SIX_HUNDRED = 600;
    public static final int ONE_SEVENTY = 170;
    public static final int _1628 = 1628;
    public static final int THREE_TWENTY = 320;
    String engineOilAndEngineOilFilter = "Engine oil and engine oil Filter";
    String driveBelt = "Drive Belt";
    String airCleanFilter = "Air Cleaner Filter";
    String valveClearance = "Valve Clearance";
    String fuelFilter = "Fuel Filter";
    String sparkPlug = "Spark Plug";
    String totalBodyCleaning = "Total Body Cleaning";
    String brakeAndClutchFluid = "Brake & Clutch Fluid";
    String engineCoolant = "Engine Coolant(Topup or specific gravity)";
    String manualTransFluid = "Manual Transaxle Fluid";
    String fourWheelAlignment = "Four Wheel Alignment";
    String fiveWheelBalancing = "Five Wheel Balancing";
    String idlerPulley = "Idler / Damper Pulley";
    String climateControlAirFilter = "Climate Control Air Filter";
    String autTransFluid = "Automatic Transaxle Fluid";
    String carScanner = "Car Scanner";
    String batteryCond = "Battery Condition & Specific Gravity";
    String frontAndRearSuspension = "front & Rear Suspension(Linkages & Ball Joints)";
    String interCoolerCleaning = "Inter Cooler Cleaning";
    String pmsCharges = "PMS Charges";
    String lubrication = "Lubrication";
    @Autowired
    private MdServiceRateCardRepository mdServiceRateCardRepository;
    @Autowired
    private MdServiceTypeRepository mdServiceTypeRepository;
    @Autowired
    private MdServiceTypeCriteriaRepository serviceTypeCriteriaRepository;
    @Autowired
    private MdMaintenanceTypeRepository mdMaintenanceTypeRepository;
    @Autowired
    private MdServiceItemRepository mdServiceItemRepository;

    @Test
    @Transactional
    @Rollback
    public void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassLoader classLoader = MdServiceRateCardRepositoryTest.class.getClassLoader();
            File file = new File(classLoader.getResource("vehicles_rate_card.json").getFile());
            String jsonPath = file.getAbsolutePath();
            String json = Files.readString(Paths.get(jsonPath));
            VehicleData[] vehicleData = objectMapper.readValue(json, VehicleData[].class);

            Iterable<MdServiceType> serviceTypes = mdServiceTypeRepository
                    .findAllById(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11));// free Service 1
            Map<Integer, MdServiceType> serviceTypeMap = StreamSupport.stream(serviceTypes.spliterator(), false)
                    .collect(toMap(MdServiceType::getId,
                            Function.identity()));

            Iterable<MdServiceTypeCriteria> allServiceTypeCriteria = serviceTypeCriteriaRepository.findAll();
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap =
                    StreamSupport.stream(allServiceTypeCriteria.spliterator(), false)
                            .collect(toMap(serviceTypeCriteria -> serviceTypeCriteria.getMdServiceType().getId(),
                                    Function.identity()));


            Map<Integer, MdServiceItem> serviceItemsMap = StreamSupport
                    .stream(mdServiceItemRepository.findAll().spliterator(), false)
                    .collect(toMap(MdServiceItem::getId, Function.identity()));

            Map<Integer, MdMaintenanceType> mdMaintenanceTypeMap = new HashMap<>();
            mdMaintenanceTypeMap.put(1, mdMaintenanceTypeRepository.findById(1).get()); // Maintenance Type I
            mdMaintenanceTypeMap.put(2, mdMaintenanceTypeRepository.findById(2).get());
            mdMaintenanceTypeMap.put(3, mdMaintenanceTypeRepository.findById(3).get());
            mdMaintenanceTypeMap.put(4, mdMaintenanceTypeRepository.findById(4).get());

            for (VehicleData data : vehicleData) {
                final String vehicle = data.getVehicle();
                final String type = data.getType();
                boolean isPetrol = type.equalsIgnoreCase("petrol") ;
                boolean isDiesel = type.equalsIgnoreCase("diesel");
                List<ServiceTypeData> serviceTypeData = data.getServiceTypeData();
                serviceTypeData.forEach(serviceType -> {
                            final int serviceTypeId = serviceType.getId();
                            List<ServiceItemData> serviceItemData = serviceType.getServiceItemData();
                            serviceItemData.forEach(serviceItem -> {
                                final int serviceItemId = serviceItem.getId();
                                final int maintenanceTypeId = serviceItem.getMaintenanceTypeId();
                                final double price = serviceItem.getPrice();
                                MdServiceRateCardBuilder mdServiceRateCardBuilder = MdServiceRateCardBuilder
                                        .aMdServiceRateCard()
                                        .withMdServiceType(serviceTypeMap.get(serviceTypeId))
                                        .withApplicableForPetrol(isPetrol)
                                        .withApplicableForDiesel(isDiesel)
                                        .withMdServiceTypeCriteria(serviceCriteriaTypeMap.get(serviceTypeId))
                                        .withMdTenant(tenantRepository.findByTenantIdentifier(TENANT_BHRTHYUND)).withVehicleModel(vehicle).withRate(price)
                                        .withMdServiceItem(serviceItemsMap.get(serviceItemId));
                                if (maintenanceTypeId != 0) {
                                    mdServiceRateCardBuilder.withMdMaintenanceType(mdMaintenanceTypeMap.get(maintenanceTypeId));
                                }
                                mdServiceRateCardRepository.save(mdServiceRateCardBuilder.build());

                            });
                        }
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Transactional
    @Rollback
    void testSave() {

        Iterable<MdServiceType> serviceTypes = mdServiceTypeRepository
                .findAllById(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11));// free Service 1
        Map<Integer, MdServiceType> serviceTypeMap = StreamSupport.stream(serviceTypes.spliterator(), false)
                .collect(toMap(MdServiceType::getId,
                        Function.identity()));

        Iterable<MdServiceTypeCriteria> allServiceTypeCriteria = serviceTypeCriteriaRepository.findAll();
        Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap =
                StreamSupport.stream(allServiceTypeCriteria.spliterator(), false)
                        .collect(toMap(serviceTypeCriteria -> serviceTypeCriteria.getMdServiceType().getId(),
                                Function.identity()));


        Map<String, MdServiceItem> serviceItemsMap = StreamSupport.stream(serviceTypes.spliterator(), false)
                .map(MdServiceType::getServiceItems).flatMap(Collection::stream).collect(
                        toMap(MdServiceItem::getName, Function.identity(), (k1, k2) -> k1));

        MdMaintenanceType mdMaintenanceType_I = mdMaintenanceTypeRepository.findById(1).get(); // Maintenance Type I
        MdMaintenanceType mdMaintenanceType_R = mdMaintenanceTypeRepository.findById(2).get();
        MdMaintenanceType mdMaintenanceType_A = mdMaintenanceTypeRepository.findById(3).get();
        MdMaintenanceType mdMaintenanceType_C = mdMaintenanceTypeRepository.findById(4).get();


        newSantroServiceItemEngineOilRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I, mdMaintenanceType_R);

        newSantroServiceItemDrivebeltRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I, mdMaintenanceType_R);

        newSantroServiceItemvalveClearanceRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I);

        newSantroServiceItemFuelFilterRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I, mdMaintenanceType_R);

        newSantroServiceItemSparkPlugRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap);

        newSantroServiceItemTotalBodyCleaningRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_C);

        newSantroServiceItemBrakeAndClutchRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I);

        newSantroServiceItemFourWheelAlignRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I);

        newSantroServiceItemFiveWheelBalancingRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I);

        newSantroServiceItemClimateControlAirFilterRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I, mdMaintenanceType_R, mdMaintenanceType_C);

        newSantroServiceItemCarScannerRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I);

        newSantroServiceItemBtryConditionRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_I);

        newSantroServiceItemLubcricationRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap);

        newSantroServiceItemFrontAndRearSuspensionsLinkagesRateCard(serviceTypeMap, serviceCriteriaTypeMap,
                serviceItemsMap, mdMaintenanceType_I);

        newSantroServiceItemPMSChargesRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap);

        newSantroServiceItemAirFilterRateCard(serviceTypeMap, serviceCriteriaTypeMap, serviceItemsMap,
                mdMaintenanceType_R, mdMaintenanceType_C);


    }

    private void newSantroServiceItemAirFilterRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_R, MdMaintenanceType mdMaintenanceType_C) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(2),
                ZERO, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(3),
                ONE_HUNDRED_SIXTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(4),
                ONE_HUNDRED_SIXTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(5),
                THREE_TWENTY, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(6),
                ONE_HUNDRED_SIXTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(7),
                ONE_HUNDRED_SIXTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(8),
                THREE_TWENTY, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(9),
                ONE_HUNDRED_SIXTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(10),
                ONE_HUNDRED_SIXTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(airCleanFilter), serviceCriteriaTypeMap.get(11),
                THREE_TWENTY, mdMaintenanceType_R, true, false);
    }

    private void newSantroServiceItemPMSChargesRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(2),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(3),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(4),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(5),
                _1628, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(6),
                _1628, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(7),
                _1628, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(8),
                _1628, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(9),
                _1628, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(10),
                _1628, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(pmsCharges), serviceCriteriaTypeMap.get(11),
                _1628, null, true, false);
    }

    private void newSantroServiceItemFrontAndRearSuspensionsLinkagesRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(2),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(3),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(4),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(5),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(6),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(7),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(8),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(9),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(10),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(frontAndRearSuspension), serviceCriteriaTypeMap.get(11),
                ZERO, mdMaintenanceType_I, true, false);
    }

    private void newSantroServiceItemLubcricationRateCard(Map<Integer, MdServiceType> serviceTypeMap, Map<Integer,
            MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(2),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(3),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(4),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(5),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(6),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(7),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(8),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(9),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(10),
                TWO_SEVENTY_FIVE, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(lubrication), serviceCriteriaTypeMap.get(11),
                TWO_SEVENTY_FIVE, null, true, false);
    }

    private void newSantroServiceItemBtryConditionRateCard(Map<Integer, MdServiceType> serviceTypeMap, Map<Integer,
            MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(2),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(3),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(4),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(5),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(6),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(7),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(8),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(9),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(10),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(batteryCond), serviceCriteriaTypeMap.get(11),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_I, true, false);
    }

    private void newSantroServiceItemCarScannerRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap,
            Map<String, MdServiceItem> serviceItemsMap, MdMaintenanceType mdMaintenanceType_I) {

        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(2),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(3),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(4),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(5),
                ONE_SEVENTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(6),
                ONE_SEVENTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(7),
                ONE_SEVENTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(8),
                ONE_SEVENTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(9),
                ONE_SEVENTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(10),
                ONE_SEVENTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(carScanner), serviceCriteriaTypeMap.get(11),
                ONE_SEVENTY, mdMaintenanceType_I, true, false);
    }

    private void newSantroServiceItemClimateControlAirFilterRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer,
                    MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I, MdMaintenanceType mdMaintenanceType_R,
            MdMaintenanceType mdMaintenanceType_C) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(2),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(3),
                ZERO, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(4),
                ZERO, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(5),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(6),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(7),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(8),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(9),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_C, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(10),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(climateControlAirFilter), serviceCriteriaTypeMap.get(11),
                ONE_HUNDRED_EIGHTY, mdMaintenanceType_C, true, false);
    }

    private void newSantroServiceItemFiveWheelBalancingRateCard(Map<Integer, MdServiceType> serviceTypeMap, Map<Integer,
            MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I) {

        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(3),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(4),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(5),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(6),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(7),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(8),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(9),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(10),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(fiveWheelBalancing), serviceCriteriaTypeMap.get(11),
                FIVE_HUNDRED, mdMaintenanceType_I, true, false);
    }

    private void newSantroServiceItemFourWheelAlignRateCard(Map<Integer, MdServiceType> serviceTypeMap, Map<Integer,
            MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I) {

        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(3),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(4),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(5),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(6),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(7),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(8),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(9),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(10),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(fourWheelAlignment), serviceCriteriaTypeMap.get(11),
                FOUR_FIFTY, mdMaintenanceType_I, true, false);
    }

    private void newSantroServiceItemBrakeAndClutchRateCard(Map<Integer, MdServiceType> serviceTypeMap, Map<Integer,
            MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(2),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(3),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(4),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(5),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(6),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(7),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(8),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(9),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(10),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(brakeAndClutchFluid), serviceCriteriaTypeMap.get(11),
                ZERO, mdMaintenanceType_I, true, false);
    }

    private void newSantroServiceItemTotalBodyCleaningRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_c) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(2),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(3),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(4),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(5),
                345, mdMaintenanceType_c, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(6),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(7),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(8),
                345, mdMaintenanceType_c, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(9),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(10),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(totalBodyCleaning), serviceCriteriaTypeMap.get(11),
                345, mdMaintenanceType_c, true, false);
    }

    private void newSantroServiceItemSparkPlugRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(2),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(3),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(4),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(5),
                ONE_HUNDRED_EIGHTY, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(6),
                SIX_HUNDRED, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(7),
                ONE_HUNDRED_EIGHTY, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(8),
                ONE_HUNDRED_EIGHTY, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(9),
                ONE_HUNDRED_EIGHTY, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(10),
                SIX_HUNDRED, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(sparkPlug), serviceCriteriaTypeMap.get(11),
                ONE_HUNDRED_EIGHTY, null, true, false);
    }

    private void newSantroServiceItemFuelFilterRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I, MdMaintenanceType mdMaintenanceType_R) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(2),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(3),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(4),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(5),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(6),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(7),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(8),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(9),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(10),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(fuelFilter), serviceCriteriaTypeMap.get(11),
                ZERO, null, true, false);
    }

    private void newSantroServiceItemvalveClearanceRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(2),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(3),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(4),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(5),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(6),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(7),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(8),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(9),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(10),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(valveClearance), serviceCriteriaTypeMap.get(11),
                ZERO, null, true, false);
    }

    private void newSantroServiceItemDrivebeltRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I, MdMaintenanceType mdMaintenanceType_R) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(2),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(3),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(4),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(5),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(6),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(7),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(8),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(9),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(10),
                ZERO, null, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(driveBelt), serviceCriteriaTypeMap.get(11),
                ZERO, mdMaintenanceType_I, true, false);
    }

    private void newSantroServiceItemEngineOilRateCard(Map<Integer, MdServiceType> serviceTypeMap,
            Map<Integer, MdServiceTypeCriteria> serviceCriteriaTypeMap, Map<String, MdServiceItem> serviceItemsMap,
            MdMaintenanceType mdMaintenanceType_I, MdMaintenanceType mdMaintenanceType_R) {
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(2),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(2),
                ZERO, mdMaintenanceType_I, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(3),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(3),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(4),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(4),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(5),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(5),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(6),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(6),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(7),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(7),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(8),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(8),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(9),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(9),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(10),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(10),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
        serviceRateCardByItem(NEW_SANTRO_PETROL, serviceTypeMap.get(11),
                serviceItemsMap.get(engineOilAndEngineOilFilter), serviceCriteriaTypeMap.get(11),
                NINE_HUNDRED, mdMaintenanceType_R, true, false);
    }

    void serviceRateCardByItem(final String vehicleName, MdServiceType mdServiceType, MdServiceItem mdServiceItem,
            MdServiceTypeCriteria serviceTypeCriteria, final double price, final MdMaintenanceType mdMaintenanceType,
            boolean petrol, boolean diesel) {

        MdServiceRateCardBuilder mdServiceRateCardBuilder = MdServiceRateCardBuilder.aMdServiceRateCard()
                .withMdServiceType(mdServiceType).withApplicableForPetrol(petrol)
                .withApplicableForDiesel(diesel)
                .withMdServiceTypeCriteria(serviceTypeCriteria)
                .withMdTenant(tenantRepository.findByTenantIdentifier(TENANT_BHRTHYUND)).withVehicleModel(vehicleName)
                .withMdServiceItem(mdServiceItem).withRate(price);

        if (mdMaintenanceType != null)
            mdServiceRateCardBuilder.withMdMaintenanceType(mdMaintenanceType);
        MdServiceRateCard mdServiceRateCard = mdServiceRateCardBuilder.build();

        mdServiceRateCardRepository.save(mdServiceRateCard);
    }

    private void serviceRateCardByItem(final String vehicleName, MdServiceType mdServiceType,
            MdMaintenanceType mdMaintenanceType_I,
            MdMaintenanceType mdMaintenanceType_R, MdMaintenanceType mdMaintenanceType_A,
            MdMaintenanceType mdMaintenanceType_C,
            RateCardPriceData rateCardPriceData) {
        Map<String, MdServiceItem> serviceItemMap = mdServiceType.getServiceItems().stream()
                .collect(toMap(MdServiceItem::getName, Function.identity()));

        MdServiceRateCardBuilder mdServiceRateCardBuilder = MdServiceRateCardBuilder.aMdServiceRateCard()
                .withMdServiceType(mdServiceType).withApplicableForPetrol(true)
                .withMdServiceTypeCriteria(serviceTypeCriteriaRepository.findById(1).get())
                .withMdTenant(tenantRepository.findByTenantIdentifier(TENANT_BHRTHYUND)).withVehicleModel(NEW_SANTRO_PETROL);
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(
                mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(engineOilAndEngineOilFilter))
                .withRate(rateCardPriceData.getEngineOilAndEngineOilFilterPrice()).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(driveBelt)).withRate(rateCardPriceData.getDriveBeltPrice())
                .build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(airCleanFilter))
                .withRate(rateCardPriceData.getAirCleanFilterPrice()).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(valveClearance))
                .withRate(rateCardPriceData.getValveClearancePrice()).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(fuelFilter)).withRate(rateCardPriceData.getFuelFilterPrice())
                .build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(sparkPlug)).withRate(rateCardPriceData.getSparkPlugPrice())
                .build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(totalBodyCleaning))
                .withRate(rateCardPriceData.getTotalBodyCleaningPrice()).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(brakeAndClutchFluid))
                .withRate(rateCardPriceData.getBrakeAndClutchFluidPrice()).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(engineCoolant))
                .withRate(rateCardPriceData.getEngineCoolantPrice()).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(climateControlAirFilter))
                .withMdMaintenanceType(mdMaintenanceType_I)
                .withRate(rateCardPriceData.getClimateControlAirFilterPrice()).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(carScanner)).withRate(rateCardPriceData.getCarScannerPrice())
                .build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(lubrication)).withRate(rateCardPriceData.getLubricationPrice())
                .build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(pmsCharges)).withRate(rateCardPriceData.getPmsChargesPrice())
                .build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(batteryCond)).withRate(rateCardPriceData.getBatteryCondPrice())
                .build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(frontAndRearSuspension))
                .withRate(rateCardPriceData.getFrontAndRearSuspensionPrice()).build());
    }

    private void setServiceCriteria(final String vehicle, MdServiceType mdServiceType,
            Map<String, MdServiceItem> serviceItemMap, final String tenant,
            MdMaintenanceType mdMaintenanceType_I, double engineOilPrice) {
        MdServiceRateCardBuilder mdServiceRateCardBuilder = MdServiceRateCardBuilder.aMdServiceRateCard()
                .withMdServiceType(mdServiceType).withApplicableForPetrol(true)
                .withMdServiceTypeCriteria(serviceTypeCriteriaRepository.findById(1).get())
                .withMdTenant(tenantRepository.findByTenantIdentifier(TENANT_BHRTHYUND)).withVehicleModel(vehicle);
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(
                mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(engineOilAndEngineOilFilter)).withRate(ZERO).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(driveBelt)).withRate(ZERO).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(airCleanFilter)).withRate(ZERO).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(valveClearance)).withRate(ZERO).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(fuelFilter)).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(sparkPlug)).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(totalBodyCleaning)).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(brakeAndClutchFluid)).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(engineCoolant)).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(climateControlAirFilter))
                .withMdMaintenanceType(mdMaintenanceType_I).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(carScanner)).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(lubrication)).withRate(275).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but()
                .withMdServiceItem(serviceItemMap.get(pmsCharges)).withRate(ZERO_DOUBLE).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(batteryCond)).withRate(180).build());
        mdServiceRateCardRepository.save(mdServiceRateCardBuilder.but().withMdMaintenanceType(mdMaintenanceType_I)
                .withMdServiceItem(serviceItemMap.get(frontAndRearSuspension)).withRate(ZERO_DOUBLE).build());
    }

    public static class VehicleData {

        private String vehicle;
        private String type;
        private List<ServiceTypeData> serviceTypeData;

        public String getVehicle() {
            return vehicle;
        }

        public void setVehicle(String vehicle) {
            this.vehicle = vehicle;
        }

        public List<ServiceTypeData> getServiceTypeData() {
            return serviceTypeData;
        }

        public void setServiceTypeData(
                List<ServiceTypeData> serviceTypeData) {
            this.serviceTypeData = serviceTypeData;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class ServiceTypeData {
        private int id;
        private List<ServiceItemData> serviceItemData;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ServiceItemData> getServiceItemData() {
            return serviceItemData;
        }

        public void setServiceItemData(
                List<ServiceItemData> serviceItemData) {
            this.serviceItemData = serviceItemData;
        }
    }

    public static class ServiceItemData {
        private int id;
        private double price;
        private int maintenanceTypeId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getMaintenanceTypeId() {
            return maintenanceTypeId;
        }

        public void setMaintenanceTypeId(int maintenanceTypeId) {
            this.maintenanceTypeId = maintenanceTypeId;
        }
    }
}