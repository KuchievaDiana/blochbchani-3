const Web3 = require('web3');

// Подключение к тестовой сети
const web3 = new Web3('https://mainnet.infura.io/v3/7959117deb4041f690d792ca5d6e24b5');

// Адрес смарт-контракта
const contractAddress = '0x12293c64efd2e8fb2788c151bb7e2f34481144c9';

// Адрес аккаунта
const walletAddress = '0x91332c3500C04a5231A4810AA0b14706D110bB36';

// ABI смарт-контракта
const contractAbi = [
    {
        "anonymous": false,
        "inputs": [
            {
                "indexed": true,
                "internalType": "bytes32",
                "name": "key",
                "type": "bytes32"
            },
            {
                "indexed": false,
                "internalType": "uint256",
                "name": "intValue",
                "type": "uint256"
            },
            {
                "indexed": true,
                "internalType": "address",
                "name": "addressValue",
                "type": "address"
            },
            {
                "indexed": false,
                "internalType": "string",
                "name": "stringValue",
                "type": "string"
            }
        ],
        "name": "StructureAdded",
        "type": "event"
    },
    {
        "anonymous": false,
        "inputs": [
            {
                "indexed": true,
                "internalType": "bytes32",
                "name": "key",
                "type": "bytes32"
            }
        ],
        "name": "StructureRemoved",
        "type": "event"
    },
    {
        "inputs": [
            {
                "internalType": "bytes32",
                "name": "key",
                "type": "bytes32"
            },
            {
                "internalType": "uint256",
                "name": "intValue",
                "type": "uint256"
            },
            {
                "internalType": "address",
                "name": "addressValue",
                "type": "address"
            },
            {
                "internalType": "string",
                "name": "stringValue",
                "type": "string"
            }
        ],
        "name": "addStructure",
        "outputs": [],
        "stateMutability": "nonpayable",
        "type": "function"
    },
    {
        "inputs": [
            {
                "internalType": "bytes32",
                "name": "",
                "type": "bytes32"
            }
        ],
        "name": "customStructures",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "intValue",
                "type": "uint256"
            },
            {
                "internalType": "address",
                "name": "addressValue",
                "type": "address"
            },
            {
                "internalType": "string",
                "name": "stringValue",
                "type": "string"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [
            {
                "internalType": "bytes32",
                "name": "key",
                "type": "bytes32"
            }
        ],
        "name": "removeStructure",
        "outputs": [],
        "stateMutability": "nonpayable",
        "type": "function"
    }
];

const contract = new web3.eth.Contract(contractAbi, contractAddress);

// Функция вызова метода контракта для добавления структуры
async function addStructureToContract() {
    const key = web3.utils.utf8ToHex('user123');
    const intValue = 123;
    const addressValue = '0x91332c3500C04a5231A4810AA0b14706D110bB36';
    const stringValue = 'Hello, Smart Contract!';

    try {
        await contract.methods.addStructure(key, intValue, addressValue, stringValue).send({ from: walletAddress });
        console.log('Структура успешно добавлена в контракт!');
    } catch (error) {
        console.error('Ошибка при добавлении структуры в контракт:', error);
    }
}

// Функция вызова метода контракта для удаления структуры
async function removeStructureFromContract() {
    const key = web3.utils.utf8ToHex('user123');

    try {
        await contract.methods.removeStructure(key).send({ from: walletAddress });
        console.log('Структура успешно удалена из контракта!');
    } catch (error) {
        console.error('Ошибка при удалении структуры из контракта:', error);
    }
}

// Функция вызова метода контракта для просмотра структуры по ключу
async function viewStructureByKey() {
    const key = web3.utils.utf8ToHex('user123');

    try {
        const structure = await contract.methods.customStructures(key).call();
        console.log('Структура по ключу:', structure);
    } catch (error) {
        console.error('Ошибка при чтении структуры из контракта:', error);
    }
}

// Вызываем функции
addStructureToContract();
removeStructureFromContract();
viewStructureByKey();
