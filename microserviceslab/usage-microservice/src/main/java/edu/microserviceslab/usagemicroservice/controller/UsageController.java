package edu.microserviceslab.usagemicroservice.controller;

import edu.microserviceslab.usagemicroservice.entity.UsageStatistic;
import edu.microserviceslab.usagemicroservice.repo.UsageStatisticRepo;
import edu.microserviceslab.usagemicroservice.service.interfaces.UsageService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usage")
public class UsageController {

    private UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }


    @ResponseBody
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public UsageStatistic addUsageStatistic(@RequestBody UsageStatistic usage) {
        if (usage == null) {
            throw new IllegalStateException("Please submit usage to add.");
        }
        if (usage.getVehicleId() == null) {
            throw new IllegalStateException("Please submit a vehicle id.");
        }

        List<UsageStatistic> list = usageService.getUsageStatisticsPerVehicle(usage.getVehicleId());

        UsageStatistic toAdd = list.get(0);

        if (toAdd.getDriverFullname() == null || toAdd.getDriverId() == null) {
            throw new IllegalStateException("No driver associated with vehicle id" + usage.getVehicleId());
        }

        usage.setDriverId(toAdd.getDriverId());
        usage.setDriverFullname(toAdd.getDriverFullname());
        usage.setVehicleLicensePlate(toAdd.getVehicleLicensePlate());

        return usage;

    }

    @ResponseBody
    @RequestMapping("/list")
    public List<UsageStatistic> listAllUsageStatistics() {
        return usageService.getAllUsageStatistics();
    }

    @ResponseBody
    @RequestMapping("/driver/{driverId}")
    public List<UsageStatistic> listAllUsageStatisticsForDriver(@PathVariable("driverId") Long driverId) {
        return usageService.getUsageStatisticsPerDriver(driverId);
    }

    @ResponseBody
    @RequestMapping("/vehicle/{vehicleId}")
    public List<UsageStatistic> listAllUsageStatisticsForVehicle(@PathVariable("vehicleId") Long vehicleId) {
        return usageService.getUsageStatisticsPerVehicle(vehicleId);
    }
}
