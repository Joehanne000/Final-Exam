package org.activity.cars.Controller;


import jakarta.validation.Valid;
import org.activity.cars.DTO.CarDTO;
import org.activity.cars.Exception.ResourceNotFoundException;
import org.activity.cars.Model.Car;
import org.activity.cars.Repository.CarRepository;
import org.activity.cars.Service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CarController {

    private final CarService carService;
    private final CarRepository carRepository;

    public CarController(CarRepository carRepository, CarService carService) {
        this.carRepository = carRepository;
        this.carService = carService;
    }

    // -------------------- LIST / SEARCH --------------------
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String search, Model model) {
        List<Car> cars;

        if (search.isEmpty()) {
            cars = carRepository.findAll();
        } else {
            cars = carRepository.findByMakeContainingIgnoreCaseOrLicensePlateNumberContainingIgnoreCaseOrColorContainingIgnoreCaseOrBodyTypeContainingIgnoreCaseOrEngineTypeContainingIgnoreCaseOrTransmissionContainingIgnoreCase(
                    search, search, search, search, search, search
            );
        }

        model.addAttribute("cars", cars);
        model.addAttribute("search", search);
        return "index";
    }

    // -------------------- DELETE --------------------
    @GetMapping("/delete")
    public String deleteCar(@RequestParam int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + id));
        carRepository.delete(car);
        return "redirect:/";
    }

    // -------------------- ADD NEW --------------------
    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("car", new CarDTO());
        populateFormAttributes(model);
        return "new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("car") @Valid CarDTO carDTO,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            populateFormAttributes(model);
            return "new";
        }

        Car car = mapDtoToCar(carDTO);
        carRepository.save(car);
        return "redirect:/";
    }

    // -------------------- EDIT --------------------
    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + id));

        CarDTO carDTO = mapCarToDto(car);
        model.addAttribute("car", carDTO);
        populateFormAttributes(model);
        return "edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("car") @Valid CarDTO carDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            populateFormAttributes(model);
            return "edit";
        }

        Car car = carRepository.findById(carDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + carDTO.getId()));

        car = mapDtoToCar(carDTO, car);
        carRepository.save(car);
        return "redirect:/";
    }

    // -------------------- HELPER METHODS --------------------
    private void populateFormAttributes(Model model) {
        model.addAttribute("body", new String[]{"Sedan", "SUV", "Hatchback", "Pickup", "Coupe", "Convertible"});
        model.addAttribute("types", new String[]{"Gasoline", "Diesel", "Electric", "Hybrid"});
        model.addAttribute("sizes", new String[]{"Automatic", "Manual"});
    }

    private Car mapDtoToCar(CarDTO dto) {
        Car car = new Car();
        return mapDtoToCar(dto, car);
    }

    private Car mapDtoToCar(CarDTO dto, Car car) {
        car.setMake(dto.getMake());
        car.setYear(dto.getYear());
        car.setLicensePlateNumber(dto.getLicensePlateNumber());
        car.setColor(dto.getColor());
        car.setBodyType(dto.getBodyType());
        car.setEngineType(dto.getEngineType());
        car.setTransmission(dto.getTransmission());
        return car;
    }

    private CarDTO mapCarToDto(Car car) {
        CarDTO dto = new CarDTO();
        dto.setId(car.getId());
        dto.setMake(car.getMake());
        dto.setYear(car.getYear());
        dto.setLicensePlateNumber(car.getLicensePlateNumber());
        dto.setColor(car.getColor());
        dto.setBodyType(car.getBodyType());
        dto.setEngineType(car.getEngineType());
        dto.setTransmission(car.getTransmission());
        return dto;
    }
}
