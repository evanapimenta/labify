package com.fatec.labify.api.dto.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatec.labify.domain.Address;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO {

    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public AddressDTO() {}

    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.complement = address.getComplement();
        this.neighborhood = address.getNeighborhood();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
        this.country = address.getCountry();
    }

    public String getStreet() {
        return street;
    }

    public AddressDTO setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public AddressDTO setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getComplement() {
        return complement;
    }

    public AddressDTO setComplement(String complement) {
        this.complement = complement;
        return this;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public AddressDTO setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
        return this;
    }

    public String getCity() {
        return city;
    }

    public AddressDTO setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public AddressDTO setState(String state) {
        this.state = state;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public AddressDTO setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public AddressDTO setCountry(String country) {
        this.country = country;
        return this;
    }
}
