package rs.edu.raf.spring_project.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.spring_project.model.entities.City;
import rs.edu.raf.spring_project.repositories.CityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CityService implements IService<City, Long>{

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<City> findAll() {
        return (List<City>) cityRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }
}
