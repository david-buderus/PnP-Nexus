import React, { useState } from "react";
import { Sidebar,  Menu, MenuItem, SubMenu } from 'react-pro-sidebar';

import { FiArrowLeftCircle, FiArrowRightCircle } from "react-icons/fi";
import { TfiWorld } from "react-icons/tfi";
import { GiDiceSixFacesFour, GiSwapBag, GiAxeSword, GiShield, GiChestArmor, GiRing } from "react-icons/gi";
import { Link, To, useSearchParams } from "react-router-dom";
import { UserPermissions } from "./interfaces/UserPermissions";
import { useTranslation } from "react-i18next";

interface MainSidebarProps {
  userPermissions: UserPermissions;
}

function buildLink(path: string, searchParams: URLSearchParams) {
  return <Link to={{
    pathname: path, 
    search: searchParams.toString()
  }} />
}

function MainSidebar(props: React.PropsWithChildren<MainSidebarProps>) {
  const { t } = useTranslation();
  const [menuCollapse, setMenuCollapse] = useState(false);
  const [searchParams] = useSearchParams();

  const menuIconClick = () => {
    setMenuCollapse(!menuCollapse);
  };

  return (
    <Sidebar collapsed={menuCollapse} backgroundColor="rgb(231 229 228)" className="border-none h-full">
      <header className="p-3">
        <div className="text-3xl font-bold">
          <p>{menuCollapse ? <GiDiceSixFacesFour/> : "P&P Nexus"}</p>
        </div>
        <div className="right-2 top-4 cursor-pointer z-50 absolute font-bold text-xl" onClick={menuIconClick}>
          {menuCollapse ? <FiArrowRightCircle/> : <FiArrowLeftCircle/>}
        </div>
      </header>
      <Menu>
        <MenuItem icon={<TfiWorld/>} component={buildLink("/universe", searchParams)}> {t("universe")} </MenuItem>
        <SubMenu label={t("items")} icon={<GiSwapBag/>} component={buildLink("/items", searchParams)}>
          <MenuItem icon={<GiAxeSword/>} component={buildLink("/weapons", searchParams)}> {t("weapons")} </MenuItem>
          <MenuItem icon={<GiShield/>} component={buildLink("/shields", searchParams)}> {t("shields")} </MenuItem>
          <MenuItem icon={<GiChestArmor/>} component={buildLink("/armor", searchParams)}> {t("armor")} </MenuItem>
          <MenuItem icon={<GiRing/>} component={buildLink("/jewellery", searchParams)}> {t("jewellery")} </MenuItem>
        </SubMenu>
        { props.userPermissions.isAdmin ? <MenuItem component={buildLink("/", searchParams)}> Home </MenuItem> : "" }
        { props.userPermissions.isAdmin ? <MenuItem component={buildLink("/about", searchParams)}> About </MenuItem> : "" }
      </Menu>
    </Sidebar>
  );
}

export default  MainSidebar;
