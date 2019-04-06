package com.clickbus.restapi.web.convert;

import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.web.dto.PlaceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PlaceConvertTest {
    private PlaceConvert placeConvert;

    @BeforeEach
    public void beforeEach() {
        this.placeConvert = new PlaceConvert();
    }

    @Nested
    public class ToDto {
        @Test
        public void withData() {

        }

        @Test
        public void emptyItem() {
            PlaceDto output = PlaceConvertTest.this.placeConvert.toDto(new Place());
            assertThat(output).isNotNull(); // TODO
        }

        @Test
        public void nullItem() {
            PlaceDto output = PlaceConvertTest.this.placeConvert.toDto((Place) null);
            assertThat(output).isNull();
        }
    }
}
