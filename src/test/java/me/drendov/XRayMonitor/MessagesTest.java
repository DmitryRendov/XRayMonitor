package me.drendov.XRayMonitor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Messages Enum Tests")
class MessagesTest {

    @Test
    @DisplayName("Should contain all expected message types")
    void testAllMessageTypesExist() {
        // Verify that key message types exist
        assertNotNull(Messages.valueOf("Reloaded"));
        assertNotNull(Messages.valueOf("NoPermissionForCommand"));
        assertNotNull(Messages.valueOf("WorldNotFound"));
        assertNotNull(Messages.valueOf("Diamond"));
        assertNotNull(Messages.valueOf("Gold"));
        assertNotNull(Messages.valueOf("Emerald"));
        assertNotNull(Messages.valueOf("VeryHighChanceXRay"));
        assertNotNull(Messages.valueOf("PotentialXrayerWarning"));
    }

    @Test
    @DisplayName("Should have correct number of message types")
    void testMessageCount() {
        // This test ensures we don't accidentally remove message types
        Messages[] allMessages = Messages.values();
        
        // Verify we have the expected number of messages (adjust as needed)
        assertTrue(allMessages.length >= 30, "Should have at least 30 message types");
    }

    @Test
    @DisplayName("Should contain ore-related messages")
    void testOreMessages() {
        Messages[] oreMessages = {
            Messages.Diamond,
            Messages.Gold,
            Messages.Emerald,
            Messages.Iron,
            Messages.Copper,
            Messages.Redstone,
            Messages.Coal,
            Messages.Lapis,
            Messages.AncientDebris
        };

        for (Messages oreMessage : oreMessages) {
            assertNotNull(oreMessage, "Ore message should not be null: " + oreMessage);
        }
    }

    @Test
    @DisplayName("Should contain X-ray detection messages")
    void testXRayDetectionMessages() {
        Messages[] xrayMessages = {
            Messages.VeryLowChanceXRay,
            Messages.LowChanceXRay,
            Messages.MediumChanceXRay,
            Messages.HighChanceXRay,
            Messages.VeryHighChanceXRay,
            Messages.PotentialXrayerWarning
        };

        for (Messages xrayMessage : xrayMessages) {
            assertNotNull(xrayMessage, "X-ray message should not be null: " + xrayMessage);
        }
    }

    @Test
    @DisplayName("Should handle invalid message name")
    void testInvalidMessageName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Messages.valueOf("NonExistentMessage");
        });
    }
}
