package me.drendov.XRayMonitor;

import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Config Tests")
class ConfigTest {

    @Mock
    private XRayMonitor mockPlugin;

    @Mock
    private FileConfiguration mockFileConfig;

    private Config config;

    @BeforeEach
    void setUp() {
        // Create Config instance with mocked plugin
        config = new Config(mockPlugin);
        
        // Setup mock behavior
        when(mockPlugin.getConfig()).thenReturn(mockFileConfig);
        when(mockPlugin.getLogger()).thenReturn(java.util.logging.Logger.getGlobal());
    }

    @Test
    @DisplayName("Should return correct boolean value for ore activity")
    void testIsActive() {
        // Given
        when(mockFileConfig.getBoolean("diamond")).thenReturn(true);
        when(mockFileConfig.getBoolean("coal")).thenReturn(false);

        // When & Then
        assertTrue(config.isActive("diamond"));
        assertFalse(config.isActive("coal"));
        
        // Verify the mock was called correctly
        verify(mockFileConfig).getBoolean("diamond");
        verify(mockFileConfig).getBoolean("coal");
    }

    @Test
    @DisplayName("Should return correct rate values")
    void testGetRate() {
        // Given
        when(mockFileConfig.getDouble("diamond_warn")).thenReturn(3.2);
        when(mockFileConfig.getDouble("diamond_confirmed")).thenReturn(3.8);
        when(mockFileConfig.getDouble("gold_warn")).thenReturn(8.0);

        // When & Then
        assertEquals(3.2, config.getRate("warn", "diamond"));
        assertEquals(3.8, config.getRate("confirmed", "diamond"));
        assertEquals(8.0, config.getRate("warn", "gold"));
        
        // Verify the mock was called correctly
        verify(mockFileConfig).getDouble("diamond_warn");
        verify(mockFileConfig).getDouble("diamond_confirmed");
        verify(mockFileConfig).getDouble("gold_warn");
    }

    @Test
    @DisplayName("Should return command string")
    void testGetCmd() {
        // Given
        String commandName = "commandOnXrayerJoin";
        String expectedCommand = "kick %player% Suspected of X-ray";
        when(mockFileConfig.getString(commandName)).thenReturn(expectedCommand);

        // When
        String result = config.getCmd(commandName);

        // Then
        assertEquals(expectedCommand, result);
        verify(mockFileConfig).getString(commandName);
    }

    @Test
    @DisplayName("Should set logger and save config")
    void testSetLogger() {
        // Given
        String loggerName = "LogBlock";

        // When
        config.setLogger(loggerName);

        // Then
        verify(mockFileConfig).set("logging_plugin", "logblock");
        verify(mockPlugin).saveConfig();
        verify(mockPlugin, atLeastOnce()).getLogger();
    }

    @Test
    @DisplayName("Should handle different ore types for isActive")
    void testIsActiveWithDifferentOres() {
        // Given
        String[] ores = {"diamond", "gold", "emerald", "iron", "copper", "redstone", "coal"};
        boolean[] expectedResults = {true, false, true, false, true, false, true};

        for (int i = 0; i < ores.length; i++) {
            when(mockFileConfig.getBoolean(ores[i])).thenReturn(expectedResults[i]);
        }

        // When & Then
        for (int i = 0; i < ores.length; i++) {
            assertEquals(expectedResults[i], config.isActive(ores[i]), 
                "Ore " + ores[i] + " should be " + expectedResults[i]);
        }
    }

    @Test
    @DisplayName("Should handle different rate types")
    void testGetRateWithDifferentTypes() {
        // Given
        when(mockFileConfig.getDouble("emerald_warn")).thenReturn(0.3);
        when(mockFileConfig.getDouble("emerald_confirmed")).thenReturn(0.5);

        // When & Then
        assertEquals(0.3, config.getRate("warn", "emerald"));
        assertEquals(0.5, config.getRate("confirmed", "emerald"));
    }
}
