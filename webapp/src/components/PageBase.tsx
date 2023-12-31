import { AuthenticationServiceApi, Universe, UniverseServiceApi, UserServiceApi } from '../api';
import { Outlet, useOutletContext, useSearchParams } from 'react-router-dom';
import { useEffect, useMemo, useState } from 'react';
import { FaUser } from "react-icons/fa";
import { Autocomplete, TextField } from '@mui/material';
import MainSidebar from './MainSidebar';
import { UserPermissions, extractUserPermissions } from './interfaces/UserPermissions';
import { useLocation } from "react-router-dom"

type UniverseContext = { universes: Universe[] | [], activeUniverse: Universe | null };
type UserContext = { userPermissions: UserPermissions };

const UNIVERSE_API = new UniverseServiceApi();
const AUTHENTICATION_API = new AuthenticationServiceApi();
const USER_API = new UserServiceApi();

function PageBase() {
  const [universes, setUniverses] = useState<Universe[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();
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
      setUniverses(response.data);
      const universe = response.data.find(universe => universe.name === searchParams.get("universe"));
      if (universe !== undefined) {
        setActiveUniverse(universe);
      }
    });
    AUTHENTICATION_API.getUsername().then(response => {
      setUsername(response.data);
    });
  }, []);

  useEffect(() => {
    if (activeUniverse) {
      searchParams.set("universe", activeUniverse.name);
      setSearchParams(searchParams);
    }
  }, [activeUniverse]);

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
              value={activeUniverse}
            />
            : ''}
          <div className='float-right'>
            <button className='text-center inline-flex items-center btn-icon h-9 w-9'>
              <FaUser />
            </button>
          </div>
        </header>
        <div className="p-2 overflow-auto max-h-[calc(100vh-55px)]">
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

function useQuery() {
  const { search } = useLocation();

  return useMemo(() => new URLSearchParams(search), [search]);
}

