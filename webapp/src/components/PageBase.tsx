import { AuthenticationServiceApi, Universe, UniverseServiceApi, UserServiceApi, GrantedUniverseAuthorityDTO, RoleAuthorityDTO } from '../api';
import { Outlet, useOutletContext } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { FaUser } from "react-icons/fa";
import { Autocomplete, TextField } from '@mui/material';
import MainSidebar from './MainSidebar';
import { UserPermissions, extractUserPermissions } from './interfaces/UserPermissions';

type UniverseContext = { universes: Universe[] | [], activeUniverse: Universe | null };
type UserContext = { userPermissions: UserPermissions };

const UNIVERSE_API = new UniverseServiceApi();
const AUTHENTICATION_API = new AuthenticationServiceApi();
const USER_API = new UserServiceApi();

function PageBase() {
  const [universes, setUniverses] = useState<Universe[]>([]);
  const [activeUniverse, setActiveUniverse] = useState<Universe>(null);
  const [username, setUsername] = useState<string>(null);
  const [userPermissions, setUserPermissions] = useState<UserPermissions>({
    isAdmin: false,
    canCreateUniverses: false,
    canReadActiveUniverse: false,
    canWriteActiveUniverse: false,
    isActiveUniverseOwner: false
  });

  useEffect(() => {
    UNIVERSE_API.getAllUniverses().then(response => {
      setUniverses(response.data)
    });
    AUTHENTICATION_API.getUsername().then(response => {
      setUsername(response.data)
    });
  }, [])

  useEffect(() => {
    if (username === null) {
      return;
    }
    USER_API.getPermissions(username).then(response => {
      setUserPermissions(extractUserPermissions(response.data, activeUniverse))
    });
  }, [activeUniverse, username]);

  return (
    <div className='flex h-full'>
      <MainSidebar userPermissions={userPermissions}></MainSidebar>
      <div className='w-full'>
        <header className="flow-root bg-stone-200 p-1">
          {universes.length > 0 ?
            <Autocomplete
              className="float-left w-60 h-11"
              options={universes}
              getOptionLabel={(option: Universe) => {
                return option?.displayName;
              }}
              isOptionEqualToValue={(option: Universe, value: Universe) => option.name === value.name}
              renderInput={(params) => <TextField {...params} className='h-10' variant='standard' size="small" label="Universe" />}
              onChange={(_, value) => {
                setActiveUniverse(value)
              }}
            />
            : ''}
          <div className=' float-right'>
            <button className='text-center inline-flex items-center btn-icon h-9 w-9'>
              <FaUser />
            </button>
          </div>
        </header>
        <div className='p-2'>
          <Outlet context={{ universes: universes, activeUniverse: activeUniverse, userPermissions: userPermissions }} />
        </div>
      </div>
    </div>
  );
}

export function getUniverseContext() {
  return useOutletContext<UniverseContext>();
}

export function getUserContext() {
  return useOutletContext<UserContext>();
}

export default PageBase;
