package org.activity.cars.Service;


import org.activity.cars.DTO.CarDTO;
import org.activity.cars.Model.Car;
import org.activity.cars.Repository.CarRepository;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void save(CarDTO carDTO) {
        Car car = new Car();
        car.setMake(carDTO.getMake());
        car.setYear(carDTO.getYear());
        car.setLicensePlateNumber(carDTO.getLicensePlateNumber());
        car.setColor(carDTO.getColor());

        carRepository.save(car);
    }
}