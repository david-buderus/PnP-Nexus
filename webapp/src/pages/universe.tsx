import React, { useEffect, useState } from 'react';
import { getUniverseContext, getUserContext } from '../components/PageBase';
import { Autocomplete, Button, Dialog, DialogActions, DialogTitle, Grid, Paper, Stack, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { UniverseServiceApi, UserServiceApi, UserUniversePermissionDTO } from '../api';
import { API_CONFIGURATION } from '../components/Constants';
import OverviewTable from '../components/OverviewTable';
import { ConfirmationDialog } from '../components/inputs/ConfirmationDialog';
import { handleValidationError } from '../components/ErrorUtils';
import { TextFieldWithErrorForAutoComplete } from '../components/inputs/TestFieldWithError';
import { NexusSelect } from '../components/inputs/NexusSelect';
import { useNavigate } from 'react-router-dom';
import { UniverseEditDialog } from '../components/universes/UniverseEditDialog';
import { NoUniverse } from './noUniverse';

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const USER_API = new UserServiceApi(API_CONFIGURATION);

interface PermissionCreationDialogProps {
  /** If the dialog is open */
  open: boolean;
  /** On close handler */
  onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "successful" | "cancel") => void;
  /** The known names */
  displayNames: string[];
}

function PermissionCreationDialog(props: PermissionCreationDialogProps) {
  const { open, onClose, displayNames } = props;
  const { activeUniverse } = getUniverseContext();
  const { t } = useTranslation();

  const [name, setName] = useState("");
  const [permission, setPermission] = useState("READ");
  const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());

  return <Dialog open={open} onClose={onClose} fullWidth data-testid="universe-permission-add-dialog">
    <DialogTitle>{t('universe:addPermission')}</DialogTitle>
    <Stack spacing={2} className="p-2">
      <Autocomplete
        fullWidth
        options={displayNames}
        isOptionEqualToValue={(option: string, value: string) => option === value}
        renderInput={(params) => <TextFieldWithErrorForAutoComplete {...params} fieldId="name" errorMap={errors} label={t("name")} />}
        value={name || null}
        onChange={(_, value) => { setName(value); }}
        data-testid="name"
      />
      <NexusSelect
        fullWidth
        label={t("permission")}
        value={permission}
        onChange={event => setPermission(event.target.value)}
        values={[{ content: "READ", label: t("permission:read") }, { content: "WRITE", label: t("permission:write") }, { content: "OWNER", label: t("permission:owner") }]}
      />
    </Stack>
    <DialogActions>
      <Button autoFocus onClick={() => onClose({}, "cancel")}>
        {t('cancel')}
      </Button>
      <Button onClick={() => {
        UNIVERSE_API.addUniversePermission(activeUniverse.name, name, permission).then(() => onClose({}, "successful")).catch(handleValidationError(setErrors));
      }}>{t('add')}</Button>
    </DialogActions>
  </Dialog>;
}

function Universe() {
  const { activeUniverse, fetchUniverses, setActiveUniverse } = getUniverseContext();
  const { userPermissions } = getUserContext();
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [universePermissions, setUniversePermissions] = useState<UserUniversePermissionDTO[]>([]);
  const [selectedPermissions, setSelectedPermissions] = useState<UserUniversePermissionDTO[keyof UserUniversePermissionDTO][]>([]);
  const [displayNames, setDisplayNames] = useState<string[]>([]);
  const [openPermissionCreationDialog, setOpenPermissionCreationDialog] = useState(false);
  const [openPermissionDeleteDialog, setOpenPermissionDeleteDialog] = useState(false);
  const [openDeleteUniverseDialog, setOpenDeleteUniverseDialog] = useState(false);
  const [openEditUniverseDialog, setOpenEditUniverseDialog] = useState(false);

  const fetchPermissions = () => {
    if (!activeUniverse) {
      setUniversePermissions([]);
    } else {
      UNIVERSE_API.getUniversePermissions(activeUniverse.name).then(response => setUniversePermissions(response.data));
    }
  };

  useEffect(fetchPermissions, [activeUniverse]);
  useEffect(() => {
    USER_API.getDisplayNames().then(response => setDisplayNames(response.data));
  }, []);

  if (activeUniverse === null) {
    return <NoUniverse />;
  }

  const settings = activeUniverse.settings;
  const currency = settings.currencyCalculation;

  return <Grid container spacing={2}>
    <Grid item>
      <Paper className='p-4 max-w-md'>
        <Stack>
          <Typography gutterBottom variant="h5" component="div" align='center'>
            {activeUniverse.displayName + " (" + activeUniverse.name + ")"}
          </Typography>
          <Typography gutterBottom variant="body2" component="div" align='left'>
            {activeUniverse.shortDescription}
          </Typography>
          {userPermissions.isActiveUniverseOwner &&
            <Stack spacing={2} direction="row" justifyContent="end">
              <Button className='btn' onClick={() => setOpenEditUniverseDialog(true)}>
                {t("edit")}
              </Button>
              <Button className='btn' color="error" onClick={() => setOpenDeleteUniverseDialog(true)}>
                {t("delete")}
              </Button>
            </Stack>
          }
          <UniverseEditDialog
            open={openEditUniverseDialog}
            universeToEdit={activeUniverse}
            onClose={(event, reason) => {
              setOpenEditUniverseDialog(false);
              if (reason === "successful") {
                fetchUniverses();
              }
            }}
          />
          <ConfirmationDialog
            title={t('universe:confirmDeletionTitle')}
            open={openDeleteUniverseDialog}
            onClose={confirmation => {
              setOpenDeleteUniverseDialog(false);
              if (!confirmation) {
                return;
              }
              UNIVERSE_API.deleteUniverse(activeUniverse.name).then(() => {
                setActiveUniverse(null);
                fetchUniverses();
                navigate("/");
              });
            }}
          />
        </Stack>
      </Paper>
    </Grid>
    <Grid item>
      <Paper className='p-4 max-w-md'>
        <Typography gutterBottom variant="h5" component="div" align='center'>
          {t("universe:settings")}
        </Typography>
        {/* WEAR */}
        <Typography gutterBottom variant="h6" component="div" align='left'>
          {t("universe:wearFactor")}
        </Typography>
        {settings.wearFactor > 0 ?
          <Typography gutterBottom variant="body2" component="div" align='left'>
            {t("universe:wearFactorDescription", { "wearFactor": settings.wearFactor })}
          </Typography>
          :
          <Typography gutterBottom variant="body2" component="div" align='left'>
            {t("universe:wearFactorDisabled")}
          </Typography>
        }
        {/* CURRENCY */}
        <Typography gutterBottom variant="h6" component="div" align='left'>
          {t("universe:currency")}
        </Typography>
        <Typography gutterBottom variant="body2" component="div" align='left'>
          {t("universe:baseCurrencyDescription", { "currency": currency.baseCurrency, "shortForm": currency.baseCurrencyShortForm })}
          {currency.calculationEntries.map((entry, index) => " " + t("universe:calculationCurrencyDescription", {
            "factor": entry.factor,
            "currency": entry.currency,
            "shortForm": entry.currencyShortForm,
            "prevCurrency": index === 0 ? currency.baseCurrency : currency.calculationEntries[index - 1].currency
          }))}
        </Typography>
      </Paper>
    </Grid>
    {userPermissions.isActiveUniverseOwner &&
      <Grid item>
        <Paper className='p-4'>
          <Typography gutterBottom variant="h5" component="div" align='center'>
            {t("universe:permissions")}
          </Typography>
          <OverviewTable
            data={universePermissions}
            id="displayName"
            sortBy="displayName"
            columns={[
              { label: t("displayName"), id: "displayName", getter: permission => permission.displayName },
              { label: t("permission"), id: "dto", getter: permission => t("permission:" + permission.dto.permission.toLocaleLowerCase()) }
            ]}
            selectedState={[selectedPermissions, setSelectedPermissions]}
          />
          <Stack
            direction="row"
            justifyContent="flex-end"
            spacing={2}
          >
            <Button className='btn' onClick={() => setOpenPermissionCreationDialog(true)}>
              {t("add")}
            </Button>
            <Button className='btn' disabled={selectedPermissions.length === 0} onClick={() => setOpenPermissionDeleteDialog(true)}>
              {t("delete")}
            </Button>
          </Stack>
          <PermissionCreationDialog
            open={openPermissionCreationDialog}
            onClose={confirmation => {
              setOpenPermissionCreationDialog(false);
              if (!confirmation) {
                return;
              }
              fetchPermissions();
            }}
            displayNames={displayNames}
          />
          <ConfirmationDialog
            title={t('universe:confirmPermissionDeletionTitle')}
            open={openPermissionDeleteDialog}
            onClose={confirmation => {
              setOpenPermissionDeleteDialog(false);
              if (!confirmation) {
                return;
              }
              selectedPermissions.forEach(selected => UNIVERSE_API.removeUniversePermission(activeUniverse.name, selected.toString()).then(fetchPermissions));
            }}
          />
        </Paper>
      </Grid>
    }
  </Grid >;
}

export default Universe;
