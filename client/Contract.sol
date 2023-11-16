// SPDX-License-Identifier: MIT
pragma solidity >=0.6.12 <0.9.0;

contract YourSmartContract {
    // Структура произвольного содержания
    struct CustomStructure {
        uint256 intValue;
        address addressValue;
        string stringValue;
    }

    // Отображение для хранения структур
    mapping(bytes32 => CustomStructure) public customStructures;

    // Событие при добавлении структуры
    event StructureAdded(bytes32 indexed key, uint256 intValue, address indexed addressValue, string stringValue);

    // Событие при удалении структуры
    event StructureRemoved(bytes32 indexed key);

    // Функция для добавления структуры в отображение
    function addStructure(
        bytes32 key,
        uint256 intValue,
        address addressValue,
        string memory stringValue
    ) external {
        CustomStructure memory newStructure = CustomStructure({
            intValue: intValue,
            addressValue: addressValue,
            stringValue: stringValue
        });

        customStructures[key] = newStructure;

        emit StructureAdded(key, intValue, addressValue, stringValue);
    }

    // Функция для удаления структуры из отображения
    function removeStructure(bytes32 key) external {
        delete customStructures[key];

        emit StructureRemoved(key);
    }
}
