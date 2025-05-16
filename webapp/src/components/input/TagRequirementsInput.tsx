import { ActionIcon, Box, Button, Group, Input, Stack, TagsInput, Text, Tooltip } from "@mantine/core";
import { TagRequirement } from "../../api";
import { FaRegTrashCan } from "react-icons/fa6";
import { useTranslation } from "react-i18next";
import { fetchAllTags } from "../Database";
import { MdOutlineHelpOutline } from "react-icons/md";


interface TagRequirementsInputProps {
    value?: TagRequirement;
    onChange?: (t: TagRequirement) => void;
}

export default function TagRequirementsInput({
    value, onChange
}: TagRequirementsInputProps) {
    const { t } = useTranslation();
    const [tags] = fetchAllTags();

    return <Stack gap={3}>
        <Tooltip label={t("upgrade:tagRequirementTooltip")}>
            <Group gap={1}>
                <Input.Label>
                    {t("upgrade:tagRequirement")}
                </Input.Label>
                <MdOutlineHelpOutline />
            </Group>
        </Tooltip>
        {value.tagRequirements.length === 0 ?
            <Text>{t("upgrade:noRequirements")}</Text>
            : null}
        {value.tagRequirements.map((requirement, index) =>
            <Group key={"requirement-" + index} wrap="nowrap">
                <Box
                    style={{ flex: 1 }}
                >
                    <TagsInput
                        value={(requirement)}
                        onChange={(newReq) => onChange({
                            tagRequirements: value.tagRequirements.map((v, i) => i !== index ? v : newReq)
                        })}
                        data={tags}
                        clearable
                    />
                </Box>
                <ActionIcon variant="outline" color="red" size="input-sm" onClick={() => onChange({
                    tagRequirements: value.tagRequirements.filter((_, i) => i !== index)
                })}>
                    <FaRegTrashCan />
                </ActionIcon>
            </Group>
        )}
        <Button
            onClick={() => onChange({
                tagRequirements: value.tagRequirements.concat([[]])
            })}
        >
            {t("upgrade:addTagRequirement")}
        </Button>
    </Stack>;
}