package uk.gov.ons.census.fwmt.jobservice.config;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;

@Component
public class CometMapper extends ConfigurableMapper {

  @Override
  public void configureFactoryBuilder(final DefaultMapperFactory.Builder builder) {
    builder.compilerStrategy(new EclipseJdtCompilerStrategy());
  }

  @Override
  protected final void configure(final MapperFactory factory) {
    factory.classMap(CaseRequest.class, ModelCase.class).byDefault().register();
  }
}
