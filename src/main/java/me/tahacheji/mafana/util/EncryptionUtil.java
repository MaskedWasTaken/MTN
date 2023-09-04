package me.tahacheji.mafana.util;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeManagerTransaction;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeOffer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

public class EncryptionUtil {
    private final String ALGORITHM = "AES";
    private final String CIPHER_INSTANCE = "AES/ECB/PKCS5Padding";
    private final Key SECRET_KEY;

    // Initialize the secret key with a constant value
    public EncryptionUtil() {
        // Replace "mafana" with your secret key value
        SECRET_KEY = getSecretKey("mafana");
    }

    public String encryptTradeOffers(List<TradeOffer> tradeOfferList) {
        if (tradeOfferList == null) {
            return null; // Handle null tradeOfferList gracefully
        }

        List<String> list = new ArrayList<>();
        for (TradeOffer tradeOffer : tradeOfferList) {
            if(tradeOffer.getTradeMarket() == null) {
                continue;
            }
            String playerUUID = tradeOffer.getPlayer().getUniqueId().toString();
            TradeMarket tradeMarket = tradeOffer.getTradeMarket();
            List<ItemStack> offer = tradeOffer.getItemOffer();
            String uuid = tradeOffer.getUuid().toString();
            String note = tradeOffer.getNote();
            String x = tradeOffer.getX();

            // Serialize each message using a custom format
            String serializedMessage = playerUUID + "|" + tradeMarket.getUuid() + "|" + encodeItems(offer) + "|" + note + "|" + x + "|" + uuid;
            list.add(serializedMessage);
        }
        return encryptList(list);
    }

    public List<TradeOffer> decryptTradeOffers(String encryptedData) {
        try {
            List<String> decryptedList = decryptToList(encryptedData);

            List<TradeOffer> decryptedMessages = new ArrayList<>();

            for (String decryptedMessage : decryptedList) {
                String[] parts = decryptedMessage.split("\\|");
                if (parts.length >= 6) {
                    UUID uuid = UUID.fromString(parts[0]);
                    Player sender = Bukkit.getPlayer(uuid);
                    String tradeUUID = parts[1];
                    String offerUUID = parts[2];
                    String note = parts[3];
                    String valueX = parts[4];
                    UUID x = UUID.fromString(parts[5]);
                    TradeMarket tradeMarket = MafanaTradeNetwork.getInstance().getTradeMarketData().getTradeMarketFromUUID(UUID.fromString(tradeUUID));
                    TradeOffer tradeOffer = new TradeOffer(tradeMarket, x, sender, decodeItems(offerUUID),
                            note, valueX);
                    decryptedMessages.add(tradeOffer);
                }
            }

            return decryptedMessages;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String encryptTradeManagerTransaction(TradeManagerTransaction tradeManagerTransaction) {
        String serializedMessage;
        String player1 = tradeManagerTransaction.getPlayer1().getUniqueId().toString();
        String player2 = tradeManagerTransaction.getPlayer2().getUniqueId().toString();

        String player1Items = encodeItems(tradeManagerTransaction.getPlayer1Items());
        String player2Items = encodeItems(tradeManagerTransaction.getPlayer2Items());

        String time = tradeManagerTransaction.getTime();

        String uuid = tradeManagerTransaction.getUuid().toString();
        serializedMessage = player1 + "|" + player2  + "|" + player1Items + "|" + player2Items + "|" + time + "|" + uuid;
        return encryptString(serializedMessage);
    }

    public TradeManagerTransaction decryptTradeManagerTransaction(String encryptedData) {
        try {
            String decryptedMessage = decryptToString(encryptedData);
            String[] parts = decryptedMessage.split("\\|");

            if (parts.length >= 6) {
                OfflinePlayer player1 = Bukkit.getOfflinePlayer(parts[0]);
                OfflinePlayer player2 = Bukkit.getOfflinePlayer(parts[1]);

                List<ItemStack> player1Items = decodeItems(parts[2]);
                List<ItemStack> player2Items = decodeItems(parts[3]);

                String time = parts[4];
                UUID uuid = UUID.fromString(parts[5]);
                return new TradeManagerTransaction(player1, player2, player1Items, player2Items, time, uuid);
            }
        } catch (Exception e) {
            // Handle decryption errors, e.g., log them
        }

        return null;
    }

    public String encryptTradeMarket(TradeMarket tradeMarket) {
        String playerUUID = tradeMarket.getPlayer().getUniqueId().toString();
        ItemStack itemStack = tradeMarket.getItem();
        List<TradeOffer> tradeOfferList = tradeMarket.getTradeOfferList();
        String note = tradeMarket.getNote();
        String claimed = Boolean.toString(tradeMarket.isClaimed());
        String uuid = tradeMarket.getUuid().toString();

        String serializedMessage;
        if(tradeOfferList != null) {
            serializedMessage = playerUUID + "|" + encodeItem(itemStack) + "|" + encryptTradeOffers(tradeOfferList) + "|" + note + "|" + claimed + "|" + uuid;
        } else {
            serializedMessage = playerUUID + "|" + encodeItem(itemStack) + "|" + "NULL" + "|" + note + "|" + claimed + "|" + uuid;
        }
        return encryptString(serializedMessage);
    }

    public TradeMarket decryptTradeMarket(String encryptedData) {
        try {
            String decryptedMessage = decryptToString(encryptedData);
            String[] parts = decryptedMessage.split("\\|");

            if (parts.length >= 6) {
                UUID uuid = UUID.fromString(parts[0]);
                ItemStack itemStack = decodeItem(parts[1]);
                List<TradeOffer> tradeOfferList = new ArrayList<>();
                if(parts[2].equalsIgnoreCase("NULL")) {
                    tradeOfferList = decryptTradeOffers(parts[2]);
                }
                String note = parts[3];
                boolean claimed = Boolean.parseBoolean(parts[4]);
                String marketUUID = parts[5];
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                TradeMarket tradeMarket = new TradeMarket(offlinePlayer, itemStack, note, claimed, marketUUID);
                tradeMarket.setTradeOfferList(tradeOfferList);
                return tradeMarket;
            }
        } catch (Exception e) {
            // Handle decryption errors, e.g., log them
        }

        return null;
    }

    public String encryptTradeOffer(TradeOffer tradeOffer) {
        String playerUUID = tradeOffer.getPlayer().getUniqueId().toString();
        TradeMarket tradeMarket = tradeOffer.getTradeMarket();
        List<ItemStack> offer = tradeOffer.getItemOffer();
        String uuid = tradeOffer.getUuid().toString();
        String note = tradeOffer.getNote();
        String x = tradeOffer.getX();

        // Serialize the TradeOffer using a custom format
        String serializedMessage = playerUUID + "|" + encryptTradeMarket(tradeMarket) + "|" + encodeItems(offer) + "|" + note + "|" + x + "|" + uuid;

        return encryptString(serializedMessage);
    }

    public TradeOffer decryptTradeOffer(String encryptedData) {
        try {
            String decryptedMessage = decryptToString(encryptedData);
            String[] parts = decryptedMessage.split("\\|");

            if (parts.length >= 6) {
                UUID uuid = UUID.fromString(parts[0]);
                Player sender = Bukkit.getPlayer(uuid);
                String tradeUUID = parts[1];
                String offerUUID = parts[2];
                String note = parts[3];
                String valueX = parts[4];
                UUID x = UUID.fromString(parts[5]);
                TradeMarket tradeMarket = decryptTradeMarket(tradeUUID);

                TradeOffer tradeOffer = new TradeOffer(tradeMarket, x, sender, decodeItems(offerUUID),
                        note, valueX);

                return tradeOffer;
            }
        } catch (Exception e) {
            // Handle decryption errors, e.g., log them
        }

        return null;
    }


    private Key getSecretKey(String keyString) {
        // Generate a secret key from the provided keyString
        byte[] keyBytes = Arrays.copyOf(keyString.getBytes(StandardCharsets.UTF_8), 16); // AES keys are 128 bits (16 bytes)
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String encryptList(List<String> list) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);

            String serializedList = String.join(",", list);
            byte[] encryptedBytes = cipher.doFinal(serializedList.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> decryptToList(String encryptedString) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedString);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);

            return Arrays.asList(decryptedString.split(","));
        } catch (Exception e) {
            return new ArrayList<>(); // Return an empty list if decryption fails
        }
    }

    public String encryptString(String data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            return null;
        }
    }

    public String decryptToString(String encryptedString) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedString);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null; // Return null if decryption fails
        }
    }

    public String encodeItem(ItemStack item) {
        return itemToBase64(item);
    }

    public ItemStack decodeItem(String data) throws Exception {
        return itemFromBase64(data);
    }

    public String itemToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save the item
            dataOutput.writeObject(item);

            // Serialize the item
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    public ItemStack itemFromBase64(String data) {
        try {
            byte[] bytes = Base64Coder.decodeLines(data);
            if (bytes == null || bytes.length == 0) {
                // Handle invalid or empty data here
                return null;
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            // Read the serialized item
            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions here, e.g., log the error or return a default ItemStack
            e.printStackTrace();
            return null;
        }
    }

    public String encodeItems(List<ItemStack> items) {
        return itemStackListToBase64(items);
    }

    public String itemStackListToBase64(List<ItemStack> items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the list
            dataOutput.writeInt(items.size());

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that list
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public List<ItemStack> decodeItems(String data) throws Exception {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            List<ItemStack> items = new ArrayList<>();

            // Read the serialized list
            int itemCount = dataInput.readInt();
            for (int i = 0; i < itemCount; i++) {
                ItemStack item = (ItemStack) dataInput.readObject();
                items.add(item);
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
