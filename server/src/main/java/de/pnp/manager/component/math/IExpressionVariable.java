package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Interface for classes which can be used as variables in {@link BinaryExpressionTree}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = PrimaryAttributeVariable.class, name = "PrimaryAttributeVariable"),
    @JsonSubTypes.Type(value = StringVariable.class, name = "StringVariable")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public sealed interface IExpressionVariable {

    /**
     * Returns the identifier which is used to represent this variable in a formula.
     */
    @JsonIgnore
    String getIdentifier();

    /**
     * Uses a primary attribute as variable
     */
    record PrimaryAttributeVariable(@DBRef @NotNull PrimaryAttribute attribute) implements IExpressionVariable {

        @Override
        public String getIdentifier() {
            return attribute.getShortName();
        }
    }

    /**
     * Uses a simple string as variable
     */
    record StringVariable(String variable) implements IExpressionVariable {

        @Override
        public String getIdentifier() {
            return variable;
        }
    }
}
