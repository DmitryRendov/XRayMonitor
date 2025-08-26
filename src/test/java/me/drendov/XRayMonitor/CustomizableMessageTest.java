package me.drendov.XRayMonitor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomizableMessage Tests")
class CustomizableMessageTest {

    private CustomizableMessage message;

    @BeforeEach
    void setUp() {
        message = new CustomizableMessage(
            Messages.Reloaded, 
            "Plugin reloaded successfully!", 
            "This message is shown when the plugin is reloaded"
        );
    }

    @Test
    @DisplayName("Should create CustomizableMessage with correct properties")
    void testCustomizableMessageCreation() {
        // Verify that all properties are set correctly
        assertEquals(Messages.Reloaded, message.id);
        assertEquals("Plugin reloaded successfully!", message.text);
        assertEquals("This message is shown when the plugin is reloaded", message.notes);
    }

    @Test
    @DisplayName("Should handle null text")
    void testCustomizableMessageWithNullText() {
        CustomizableMessage nullTextMessage = new CustomizableMessage(
            Messages.NoPermissionForCommand, 
            null, 
            "Test note"
        );
        
        assertEquals(Messages.NoPermissionForCommand, nullTextMessage.id);
        assertNull(nullTextMessage.text);
        assertEquals("Test note", nullTextMessage.notes);
    }

    @Test
    @DisplayName("Should handle null notes")
    void testCustomizableMessageWithNullNotes() {
        CustomizableMessage nullNotesMessage = new CustomizableMessage(
            Messages.WorldNotFound, 
            "World not found", 
            null
        );
        
        assertEquals(Messages.WorldNotFound, nullNotesMessage.id);
        assertEquals("World not found", nullNotesMessage.text);
        assertNull(nullNotesMessage.notes);
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testCustomizableMessageWithEmptyStrings() {
        CustomizableMessage emptyMessage = new CustomizableMessage(
            Messages.CalcPlayerOre, 
            "", 
            ""
        );
        
        assertEquals(Messages.CalcPlayerOre, emptyMessage.id);
        assertEquals("", emptyMessage.text);
        assertEquals("", emptyMessage.notes);
    }

    @Test
    @DisplayName("Should work with all message types")
    void testAllMessageTypes() {
        // Test with a few different message types
        Messages[] testMessages = {
            Messages.Diamond,
            Messages.Gold,
            Messages.HighChanceXRay,
            Messages.PotentialXrayerWarning
        };

        for (Messages msgType : testMessages) {
            CustomizableMessage testMsg = new CustomizableMessage(
                msgType, 
                "Test text for " + msgType, 
                "Test note for " + msgType
            );
            
            assertEquals(msgType, testMsg.id);
            assertEquals("Test text for " + msgType, testMsg.text);
            assertEquals("Test note for " + msgType, testMsg.notes);
        }
    }
}
