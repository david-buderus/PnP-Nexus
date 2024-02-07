import { getUniverseContext } from '../../components/PageBase';
import { currencyToHumanReadable } from '../../components/Utils';
import { useTranslation } from 'react-i18next';
import { ItemBasePage } from '../../components/items/ItemBasePage';


const JewelleryPage = () => {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    return <ItemBasePage itemClass={'Jewellery'} columns={[
        { label: t("name"), id: "name", getter: item => item.name },
        { label: t("item:type"), id: "type", getter: item => item.type.name },
        { label: t("item:subtype"), id: "subtype", getter: item => item.subtype.name },
        { label: t("material"), id: "material", getter: item => item.material.name },
        { label: t("effect"), id: "effect", getter: item => item.effect },
        { label: t("rarity"), id: "rarity", getter: item => t("enum:" + item.rarity.toLowerCase()) },
        { label: t("tier"), id: "tier", getter: item => item.tier, numeric: true },
        { label: t("description"), id: "description", getter: item => item.description },
        { label: t("upgradeSlots"), id: "upgradeSlots", getter: item => item.upgradeSlots, numeric: true },
        { label: t("requirement"), id: "requirement", getter: item => item.requirement },
        { label: t("price"), id: "vendorPrice", getter: item => currencyToHumanReadable(activeUniverse, item.vendorPrice) },
        { label: t("item:minStackSize"), id: "minimumStackSize", getter: item => item.minimumStackSize, numeric: true, defaultVisible: false },
        { label: t("item:maxStackSize"), id: "maximumStackSize", getter: item => item.maximumStackSize, numeric: true, defaultVisible: false },
        { label: t("note"), id: "note", getter: item => item.note, defaultVisible: false }
    ]}
    />;
};

export default JewelleryPage;
