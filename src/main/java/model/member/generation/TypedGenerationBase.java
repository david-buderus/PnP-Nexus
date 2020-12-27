package model.member.generation;

import java.util.ArrayList;
import java.util.Collection;

public abstract class TypedGenerationBase<SubType extends GenerationBase> extends GenerationBase {

    protected Collection<SubType> subTypes;
    protected Collection<TypedGenerationBase<SubType>> parents;

    public TypedGenerationBase() {
        super();
        subTypes = new ArrayList<>();
        parents = new ArrayList<>();
    }

    public Collection<SubType> getSubTypes() {
        Collection<SubType> result = new ArrayList<>(subTypes);
        for (TypedGenerationBase<SubType> parent : parents) {
           result.addAll(parent.getSubTypes());
        }
        return result;
    }

    public void addSubType(SubType subType) {
        this.subTypes.add(subType);
    }

    public SubType getSubType() {
        Collection<SubType> subTypes = getSubTypes();
        return subTypes.stream().skip(random.nextInt(subTypes.size())).findFirst().orElse(null);
    }

    @Override
    public Collection<? extends GenerationBase> getParents() {
        return this.parents;
    }

    public void addParent(TypedGenerationBase<SubType> parent) {
        this.parents.add(parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addParent(GenerationBase parent) {
        if (parent instanceof TypedGenerationBase) {
            this.addParent((TypedGenerationBase<SubType>) parent);
        } else {
            throw new IllegalArgumentException("A normal GenerationBase can't be a parent of a TypedGenerationBase");
        }
    }
}
