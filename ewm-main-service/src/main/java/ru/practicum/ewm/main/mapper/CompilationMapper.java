package ru.practicum.ewm.main.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.main.dto.CompilationDto;
import ru.practicum.ewm.main.dto.NewCompilationDto;
import ru.practicum.ewm.main.entity.Compilation;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation newCompilationDtoToCompilation(NewCompilationDto compilationDto) {
        if (compilationDto == null) {
            return null;
        } else {
            Compilation compilation = new Compilation();
            compilation.setPinned(compilationDto.getPinned());
            compilation.setTitle(compilationDto.getTitle());
            return compilation;
        }
    }

    public CompilationDto compilationToCompilationDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        } else {
            CompilationDto compilationDTO = new CompilationDto();
            if (compilation.getId() != null) {
                compilationDTO.setId(compilation.getId().intValue());
            }

            compilationDTO.setEvents(this.eventMapper.listEventToSetEventShortDto(compilation.getEvents()));
            compilationDTO.setPinned(compilation.getPinned());
            compilationDTO.setTitle(compilation.getTitle());
            return compilationDTO;
        }
    }
}
