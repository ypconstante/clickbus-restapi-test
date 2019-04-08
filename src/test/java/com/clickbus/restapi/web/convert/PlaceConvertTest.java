package com.clickbus.restapi.web.convert;

import java.util.Arrays;
import java.util.Collection;

import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.entity.testdata.PlaceTestData;
import com.clickbus.restapi.web.dto.PlaceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PlaceConvertTest {
    private PlaceConvert placeConvert;

    @BeforeEach
    void beforeEach() {
        this.placeConvert = new PlaceConvert();
    }

    @Nested
    class ToDto {
        @Test
        void item() {
            Place input = PlaceTestData.get().setId(123L);
            PlaceDto output = PlaceConvertTest.this.placeConvert.toDto(input);
            assertThat(output).isEqualTo(
                new PlaceDto()
                    .setId(123L)
                    .setName("Terminal Central")
                    .setSlug("terminal-central")
                    .setCity(
                        new PlaceDto.City().setName("Joinville")
                    )
                    .setState(
                        new PlaceDto.State().setName("São Paulo")
                    )
                    .setCountry(
                        new PlaceDto.Country().setName("México")
                    )
                    .setClientIds(Arrays.asList(1L, 2L, 4L))
            );
        }

        @Test
        void collection() {
            Collection<Place> input = Arrays.asList(
                new Place().setName("place 1"),
                new Place().setName("place 2")
            );
            Collection<PlaceDto> output = PlaceConvertTest.this.placeConvert.toDto(input);
            assertThat(output).containsExactly(
                new PlaceDto().setName("place 1"),
                new PlaceDto().setName("place 2")
            );
        }

        @Test
        void emptyItem() {
            PlaceDto output = PlaceConvertTest.this.placeConvert.toDto(new Place());
            assertThat(output).isEqualTo(new PlaceDto());
        }

        @Test
        void nullItem() {
            PlaceDto output = PlaceConvertTest.this.placeConvert.toDto((Place) null);
            assertThat(output).isNull();
        }
    }
}
