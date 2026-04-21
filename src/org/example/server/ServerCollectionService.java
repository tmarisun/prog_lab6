package org.example.server;

import lombok.Getter;
import org.example.Application;
import org.example.data.City;
import org.example.data.StandardOfLiving;
import org.example.net.protocol.CommandResponse;
import org.example.service.JsonFileLoader;
import org.example.service.JsonFileSaver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class ServerCollectionService {
    private final String fileName;
    private final Stack<City> cities;
    @Getter
    public boolean isRunning;
    public ServerCollectionService(String fileName) throws IOException {
        this.fileName = fileName;
        this.cities = JsonFileLoader.loadCollection(fileName);
    }

    public void save() throws IOException {
        JsonFileSaver.saveCitiesToFile(cities, fileName);
    }

    public String info() {
        return "Type: Stack, size: " + cities.size();
    }

    public void show(){
        if (Application.getCityStack().isEmpty()) {
            System.out.println("Collection is empty.");
            return;
        }
        for (City city : Application.getCityStack()) {
            System.out.println(city);
        }
    }

    public void exit() throws IOException{
        JsonFileSaver.saveCitiesToFile(cities, fileName);
        isRunning = false;
        System.out.println("[SERVER] Shutting down server and disconnecting all clients...");
        //System.exit(0);
    }

     public List<City> getSortedByName() {
        List<City> sorted = new ArrayList<>(cities);
        sorted.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return sorted;
    }

    public City add(City city) {
        city.setId(nextId());
        city.setCreationDate(new Date());
        cities.push(city);
        return city;
    }

    public boolean addIfMax(City city) {
        long currentMax = 0;
        for (City current : cities) {
            if (current.getId() > currentMax) {
                currentMax = current.getId();
            }
        }
        if (city.getId() != null && city.getId() <= currentMax) {
            return false;
        }
        city.setId(nextId());
        city.setCreationDate(new Date());
        cities.push(city);
        return true;
    }

    public boolean insertAt(int index, City city) {
        if (index < 0 || index > cities.size()) return false;
        city.setId(nextId());
        city.setCreationDate(new Date());
        cities.add(index, city);
        return true;
    }

    public boolean removeById(long id) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getId() == id) {
                cities.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean update(long id, City patch) {
        City target = null;
        for (City city : cities) {
            if (city.getId() == id) {
                target = city;
                break;
            }
        }
        if (target == null) {
            return false;
        }
        target.setName(patch.getName());
        target.setCoordinates(patch.getCoordinates());
        target.setArea(patch.getArea());
        target.setPopulation(patch.getPopulation());
        target.setMetersAboveSeaLevel(patch.getMetersAboveSeaLevel());
        target.setClimate(patch.getClimate());
        target.setGovernment(patch.getGovernment());
        target.setStandardOfLiving(patch.getStandardOfLiving());
        target.setGovernor(patch.getGovernor());
        return true;
    }

    public void clear() {
        cities.clear();
    }

    public void sortNatural() {
        List<City> sorted = new ArrayList<>(cities);
        sorted.sort(null);
        cities.clear();
        cities.addAll(sorted);
    }

    public long countLessThan(StandardOfLiving value) {
        long count = 0;
        for (City city : cities) {
            StandardOfLiving cityValue = city.getStandardOfLiving();
            if (cityValue != null && cityValue.getRank() > value.getRank()) {
                count++;
            }
        }
        return count;
    }

    public List<City> filterByGovernor(String governorText) {
        List<City> filtered = new ArrayList<>();
        for (City city : cities) {
            if (city.getGovernor() == null) {
                continue;
            }
            if (city.getGovernor().toString().contains(governorText)) {
                filtered.add(city);
            }
        }
        return filtered;
    }

    public List<StandardOfLiving> getStandardsAscending() {
        List<StandardOfLiving> values = new ArrayList<>();
        for (City city : cities) {
            if (city.getStandardOfLiving() != null) {
                values.add(city.getStandardOfLiving());
            }
        }
        values.sort((a, b) -> Integer.compare(b.getRank(), a.getRank()));
        return values;
    }

    private long nextId() {
        long maxId = 0;
        for (City city : cities) {
            if (city.getId() > maxId) {
                maxId = city.getId();
            }
        }
        return maxId + 1;
    }
}

