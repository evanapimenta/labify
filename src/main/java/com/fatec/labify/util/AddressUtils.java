package com.fatec.labify.util;

import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Address;

import java.util.Optional;

public class AddressUtils {

    public static Address updateAddress(Address address, AddressDTO dto) {
        Optional.ofNullable(dto.getStreet()).ifPresent(address::setStreet);
        Optional.ofNullable(dto.getNumber()).ifPresent(address::setNumber);
        Optional.ofNullable(dto.getComplement()).ifPresent(address::setComplement);
        Optional.ofNullable(dto.getNeighborhood()).ifPresent(address::setNeighborhood);
        Optional.ofNullable(dto.getCity()).ifPresent(address::setCity);
        Optional.ofNullable(dto.getState()).ifPresent(address::setState);
        Optional.ofNullable(dto.getCountry()).ifPresent(address::setCountry);
        Optional.ofNullable(dto.getZipCode()).ifPresent(address::setZipCode);

        return address;
    }

}
