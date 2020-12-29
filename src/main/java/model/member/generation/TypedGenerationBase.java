package model.member.generation;

import java.util.ArrayList;
import java.util.Collection;

public abstract class TypedGenerationBase<SubType extends GenerationBase> extends GenerationBase {

    protected Collection<SubType> subTypes;

    public TypedGenerationBase() {
        super();
        subTypes = new ArrayList<>();
        parents = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public Collection<SubType> getSubTypes() {
        Collection<SubType> result = new ArrayList<>(subTypes);
        for (GenerationBase parent : parents) {
           result.addAll(((TypedGenerationBase<SubType>) parent).getSubTypes());
        }
        return result;
    }

    public void addSubType(SubType subType) {
        this.subTypes.add(subType);
    }

    public SubType getRandomSubType() {
        Collection<SubType> subTypes = getSubTypes();
        return subTypes.stream().skip(random.nextInt(subTypes.size())).findFirst().orElse(null);
    }
}
