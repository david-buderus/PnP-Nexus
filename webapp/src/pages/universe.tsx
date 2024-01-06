import React from 'react';
import { getUniverseContext } from '../components/PageBase';
import { TextField } from '@mui/material';

function Universe() {
  const { activeUniverse } = getUniverseContext();

  if (activeUniverse === null) {
    return <div>
      Nothing here
    </div>;
  }

  return <div>
    <TextField
      id="name"
      label="ID"
      value={activeUniverse.name}
      InputProps={{
        readOnly: true
      }}
      variant="standard"
    />
    <TextField
      id="displayName"
      label="Name"
      value={activeUniverse.displayName}
      variant="standard"
    />
  </div>;
}

export default Universe;
