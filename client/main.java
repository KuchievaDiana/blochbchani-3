import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class SmartContractInteraction {

    // Адрес смарт-контракта
    private static final String contractAddress = "0x12293c64efd2e8fb2788c151bb7e2f34481144c9";

    // Адрес кошелька
    private static final String walletAddress = "0x91332c3500C04a5231A4810AA0b14706D110bB36";

    // Адрес узла Ethereum
    private static final String ethereumNodeUrl = "https://mainnet.infura.io/v3/7959117deb4041f690d792ca5d6e24b5";

    // ABI смарт-контракта
    private static final String contractAbi = [
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

    public static void main(String[] args) {
        Web3j web3j = Web3j.build(new HttpService(ethereumNodeUrl));


        Credentials credentials = Credentials.create("private-key");


        RawTransactionManager transactionManager = new RawTransactionManager(
                web3j, credentials, new NoOpProcessor(web3j));

        // Вызываем функции
        addStructureToContract(web3j, transactionManager);
        removeStructureFromContract(web3j, transactionManager);
        viewStructureByKey(web3j);
    }

    private static void addStructureToContract(Web3j web3j, RawTransactionManager transactionManager) {
        // Вызов метода контракта для добавления структуры
        String key = "user123";
        BigInteger intValue = BigInteger.valueOf(123);
        String addressValue = "0x91332c3500C04a5231A4810AA0b14706D110bB36";
        String stringValue = "Hello, Smart Contract!";

        try {
            // Создаем транзакцию
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    getNonce(web3j, walletAddress),
                    BigInteger.valueOf(18_000_000_000L), // gas price
                    BigInteger.valueOf(50_000), // gas limit
                    contractAddress,
                    contractAbi,
                    "addStructure",
                    key, intValue, addressValue, stringValue
            );

            // Подписываем транзакцию
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, transactionManager);

            // Отправляем транзакцию
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            // Обрабатываем результат
            if (ethSendTransaction.hasError()) {
                System.out.println("Ошибка при добавлении структуры в контракт: " + ethSendTransaction.getError().getMessage());
            } else {
                System.out.println("Структура успешно добавлена в контракт!");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении структуры в контракт: " + e.getMessage());
        }
    }

    private static void removeStructureFromContract(Web3j web3j, RawTransactionManager transactionManager) {
        // Вызов метода контракта для удаления структуры
        String key = "user123";

        try {
            // Создаем транзакцию
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    getNonce(web3j, walletAddress),
                    BigInteger.valueOf(18_000_000_000L), // gas price
                    BigInteger.valueOf(50_000), // gas limit
                    contractAddress,
                    contractAbi,
                    "removeStructure",
                    key
            );

            // Подписываем транзакцию
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, transactionManager);

            // Отправляем транзакцию
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            // Обрабатываем результат
            if (ethSendTransaction.hasError()) {
                System.out.println("Ошибка при удалении структуры из контракта: " + ethSendTransaction.getError().getMessage());
            } else {
                System.out.println("Структура успешно удалена из контракта!");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении структуры из контракта: " + e.getMessage());
        }
    }

    private static void viewStructureByKey(Web3j web3j) {
        // Вызов метода контракта для просмотра структуры по ключу
        String key = "user123";

        try {
            // Получаем структуру по ключу
            Object structure = web3j.ethCall(
                    Transaction.createEthCallTransaction(walletAddress, contractAddress,
                            contractAbi, "customStructures", key),
                    org.web3j.protocol.core.methods.response.EthCall.class)
                    .send()
                    .getValue();

            // Обрабатываем результат
            System.out.println("Структура по ключу: " + structure);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении структуры из контракта: " + e.getMessage());
        }
    }

    private static BigInteger getNonce(Web3j web3j, String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address, org.web3j.protocol.core.DefaultBlockParameterName.PENDING).send();

        return ethGetTransactionCount.getTransactionCount();
    }
}
