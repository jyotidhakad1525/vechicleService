package com.automate.vehicleservices.repository;

public class RateCardPriceData {
    private final double engineOilAndEngineOilFilterPrice;
    private final double driveBeltPrice;
    private final double airCleanFilterPrice;
    private final double valveClearancePrice;
    private final double fuelFilterPrice;
    private final double sparkPlugPrice;
    private final double totalBodyCleaningPrice;
    private final double brakeAndClutchFluidPrice;
    private final double engineCoolantPrice;
    private final double manualTransFluidPrice;
    private final double fourWheelAlignmentPrice;
    private final double fiveWheelBalancingPrice;
    private final double idlerPulleyPrice;
    private final double climateControlAirFilterPrice;
    private final double autTransFluidPrice;
    private final double carScannerPrice;
    private final double batteryCondPrice;
    private final double frontAndRearSuspensionPrice;
    private final double interCoolerCleaningPrice;
    private final double pmsChargesPrice;
    private final double lubricationPrice;

    private RateCardPriceData(double engineOilAndEngineOilFilterPrice, double driveBeltPrice,
            double airCleanFilterPrice, double valveClearancePrice, double fuelFilterPrice, double sparkPlugPrice,
            double totalBodyCleaningPrice, double brakeAndClutchFluidPrice, double engineCoolantPrice,
            double manualTransFluidPrice, double fourWheelAlignmentPrice, double fiveWheelBalancingPrice,
            double idlerPulleyPrice, double climateControlAirFilterPrice, double autTransFluidPrice,
            double carScannerPrice, double batteryCondPrice, double frontAndRearSuspensionPrice,
            double interCoolerCleaningPrice, double pmsChargesPrice, double lubricationPrice) {
        this.engineOilAndEngineOilFilterPrice = engineOilAndEngineOilFilterPrice;
        this.driveBeltPrice = driveBeltPrice;
        this.airCleanFilterPrice = airCleanFilterPrice;
        this.valveClearancePrice = valveClearancePrice;
        this.fuelFilterPrice = fuelFilterPrice;
        this.sparkPlugPrice = sparkPlugPrice;
        this.totalBodyCleaningPrice = totalBodyCleaningPrice;
        this.brakeAndClutchFluidPrice = brakeAndClutchFluidPrice;
        this.engineCoolantPrice = engineCoolantPrice;
        this.manualTransFluidPrice = manualTransFluidPrice;
        this.fourWheelAlignmentPrice = fourWheelAlignmentPrice;
        this.fiveWheelBalancingPrice = fiveWheelBalancingPrice;
        this.idlerPulleyPrice = idlerPulleyPrice;
        this.climateControlAirFilterPrice = climateControlAirFilterPrice;
        this.autTransFluidPrice = autTransFluidPrice;
        this.carScannerPrice = carScannerPrice;
        this.batteryCondPrice = batteryCondPrice;
        this.frontAndRearSuspensionPrice = frontAndRearSuspensionPrice;
        this.interCoolerCleaningPrice = interCoolerCleaningPrice;
        this.pmsChargesPrice = pmsChargesPrice;
        this.lubricationPrice = lubricationPrice;
    }

    public double getEngineOilAndEngineOilFilterPrice() {
        return engineOilAndEngineOilFilterPrice;
    }

    public double getDriveBeltPrice() {
        return driveBeltPrice;
    }

    public double getAirCleanFilterPrice() {
        return airCleanFilterPrice;
    }

    public double getValveClearancePrice() {
        return valveClearancePrice;
    }

    public double getFuelFilterPrice() {
        return fuelFilterPrice;
    }

    public double getSparkPlugPrice() {
        return sparkPlugPrice;
    }

    public double getTotalBodyCleaningPrice() {
        return totalBodyCleaningPrice;
    }

    public double getBrakeAndClutchFluidPrice() {
        return brakeAndClutchFluidPrice;
    }

    public double getEngineCoolantPrice() {
        return engineCoolantPrice;
    }

    public double getManualTransFluidPrice() {
        return manualTransFluidPrice;
    }

    public double getFourWheelAlignmentPrice() {
        return fourWheelAlignmentPrice;
    }

    public double getFiveWheelBalancingPrice() {
        return fiveWheelBalancingPrice;
    }

    public double getIdlerPulleyPrice() {
        return idlerPulleyPrice;
    }

    public double getClimateControlAirFilterPrice() {
        return climateControlAirFilterPrice;
    }

    public double getAutTransFluidPrice() {
        return autTransFluidPrice;
    }

    public double getCarScannerPrice() {
        return carScannerPrice;
    }

    public double getBatteryCondPrice() {
        return batteryCondPrice;
    }

    public double getFrontAndRearSuspensionPrice() {
        return frontAndRearSuspensionPrice;
    }

    public double getInterCoolerCleaningPrice() {
        return interCoolerCleaningPrice;
    }

    public double getPmsChargesPrice() {
        return pmsChargesPrice;
    }

    public double getLubricationPrice() {
        return lubricationPrice;
    }

    public static final class RateCardPriceDataBuilder {
        private double engineOilAndEngineOilFilterPrice;
        private double driveBeltPrice;
        private double airCleanFilterPrice;
        private double valveClearancePrice;
        private double fuelFilterPrice;
        private double sparkPlugPrice;
        private double totalBodyCleaningPrice;
        private double brakeAndClutchFluidPrice;
        private double engineCoolantPrice;
        private double manualTransFluidPrice;
        private double fourWheelAlignmentPrice;
        private double fiveWheelBalancingPrice;
        private double idlerPulleyPrice;
        private double climateControlAirFilterPrice;
        private double autTransFluidPrice;
        private double carScannerPrice;
        private double batteryCondPrice;
        private double frontAndRearSuspensionPrice;
        private double interCoolerCleaningPrice;
        private double pmsChargesPrice;
        private double lubricationPrice;

        private RateCardPriceDataBuilder() {
        }

        public static RateCardPriceDataBuilder aRateCardPriceData() {
            return new RateCardPriceDataBuilder();
        }

        public RateCardPriceDataBuilder withEngineOilAndEngineOilFilterPrice(double engineOilAndEngineOilFilterPrice) {
            this.engineOilAndEngineOilFilterPrice = engineOilAndEngineOilFilterPrice;
            return this;
        }

        public RateCardPriceDataBuilder withDriveBeltPrice(double driveBeltPrice) {
            this.driveBeltPrice = driveBeltPrice;
            return this;
        }

        public RateCardPriceDataBuilder withAirCleanFilterPrice(double airCleanFilterPrice) {
            this.airCleanFilterPrice = airCleanFilterPrice;
            return this;
        }

        public RateCardPriceDataBuilder withValveClearancePrice(double valveClearancePrice) {
            this.valveClearancePrice = valveClearancePrice;
            return this;
        }

        public RateCardPriceDataBuilder withFuelFilterPrice(double fuelFilterPrice) {
            this.fuelFilterPrice = fuelFilterPrice;
            return this;
        }

        public RateCardPriceDataBuilder withSparkPlugPrice(double sparkPlugPrice) {
            this.sparkPlugPrice = sparkPlugPrice;
            return this;
        }

        public RateCardPriceDataBuilder withTotalBodyCleaningPrice(double totalBodyCleaningPrice) {
            this.totalBodyCleaningPrice = totalBodyCleaningPrice;
            return this;
        }

        public RateCardPriceDataBuilder withBrakeAndClutchFluidPrice(double brakeAndClutchFluidPrice) {
            this.brakeAndClutchFluidPrice = brakeAndClutchFluidPrice;
            return this;
        }

        public RateCardPriceDataBuilder withEngineCoolantPrice(double engineCoolantPrice) {
            this.engineCoolantPrice = engineCoolantPrice;
            return this;
        }

        public RateCardPriceDataBuilder withManualTransFluidPrice(double manualTransFluidPrice) {
            this.manualTransFluidPrice = manualTransFluidPrice;
            return this;
        }

        public RateCardPriceDataBuilder withFourWheelAlignmentPrice(double fourWheelAlignmentPrice) {
            this.fourWheelAlignmentPrice = fourWheelAlignmentPrice;
            return this;
        }

        public RateCardPriceDataBuilder withFiveWheelBalancingPrice(double fiveWheelBalancingPrice) {
            this.fiveWheelBalancingPrice = fiveWheelBalancingPrice;
            return this;
        }

        public RateCardPriceDataBuilder withIdlerPulleyPrice(double idlerPulleyPrice) {
            this.idlerPulleyPrice = idlerPulleyPrice;
            return this;
        }

        public RateCardPriceDataBuilder withClimateControlAirFilterPrice(double climateControlAirFilterPrice) {
            this.climateControlAirFilterPrice = climateControlAirFilterPrice;
            return this;
        }

        public RateCardPriceDataBuilder withAutTransFluidPrice(double autTransFluidPrice) {
            this.autTransFluidPrice = autTransFluidPrice;
            return this;
        }

        public RateCardPriceDataBuilder withCarScannerPrice(double carScannerPrice) {
            this.carScannerPrice = carScannerPrice;
            return this;
        }

        public RateCardPriceDataBuilder withBatteryCondPrice(double batteryCondPrice) {
            this.batteryCondPrice = batteryCondPrice;
            return this;
        }

        public RateCardPriceDataBuilder withFrontAndRearSuspensionPrice(double frontAndRearSuspensionPrice) {
            this.frontAndRearSuspensionPrice = frontAndRearSuspensionPrice;
            return this;
        }

        public RateCardPriceDataBuilder withInterCoolerCleaningPrice(double interCoolerCleaningPrice) {
            this.interCoolerCleaningPrice = interCoolerCleaningPrice;
            return this;
        }

        public RateCardPriceDataBuilder withPmsChargesPrice(double pmsChargesPrice) {
            this.pmsChargesPrice = pmsChargesPrice;
            return this;
        }

        public RateCardPriceDataBuilder withLubricationPrice(double lubricationPrice) {
            this.lubricationPrice = lubricationPrice;
            return this;
        }

        public RateCardPriceDataBuilder but() {
            return aRateCardPriceData()
                    .withEngineOilAndEngineOilFilterPrice(engineOilAndEngineOilFilterPrice)
                    .withDriveBeltPrice(driveBeltPrice).withAirCleanFilterPrice(airCleanFilterPrice)
                    .withValveClearancePrice(valveClearancePrice).withFuelFilterPrice(fuelFilterPrice)
                    .withSparkPlugPrice(sparkPlugPrice).withTotalBodyCleaningPrice(totalBodyCleaningPrice)
                    .withBrakeAndClutchFluidPrice(brakeAndClutchFluidPrice).withEngineCoolantPrice(engineCoolantPrice)
                    .withManualTransFluidPrice(manualTransFluidPrice)
                    .withFourWheelAlignmentPrice(fourWheelAlignmentPrice)
                    .withFiveWheelBalancingPrice(fiveWheelBalancingPrice).withIdlerPulleyPrice(idlerPulleyPrice)
                    .withClimateControlAirFilterPrice(climateControlAirFilterPrice)
                    .withAutTransFluidPrice(autTransFluidPrice).withCarScannerPrice(carScannerPrice)
                    .withBatteryCondPrice(batteryCondPrice).withFrontAndRearSuspensionPrice(frontAndRearSuspensionPrice)
                    .withInterCoolerCleaningPrice(interCoolerCleaningPrice).withPmsChargesPrice(pmsChargesPrice)
                    .withLubricationPrice(lubricationPrice);
        }

        public RateCardPriceData build() {
            return new RateCardPriceData(engineOilAndEngineOilFilterPrice,
                    driveBeltPrice, airCleanFilterPrice, valveClearancePrice, fuelFilterPrice, sparkPlugPrice,
                    totalBodyCleaningPrice, brakeAndClutchFluidPrice, engineCoolantPrice, manualTransFluidPrice,
                    fourWheelAlignmentPrice, fiveWheelBalancingPrice, idlerPulleyPrice, climateControlAirFilterPrice,
                    autTransFluidPrice, carScannerPrice, batteryCondPrice, frontAndRearSuspensionPrice,
                    interCoolerCleaningPrice, pmsChargesPrice, lubricationPrice);
        }
    }
}
