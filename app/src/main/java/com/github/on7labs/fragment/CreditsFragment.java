package com.github.on7labs.fragment;


import com.github.on7labs.adapter.AbstractGithubAdapter;
import com.github.on7labs.adapter.CreditsAdapter;
import com.github.on7labs.util.Config;

/**
 * Created by: Sumit
 * Date: 19.10.2016 12:43 AM
 */

public class CreditsFragment extends AbstractGithubFragment
{
    @Override
    public String onTargetURL()
    {
        return Config.URL_CONTRIBUTORS;
    }

    @Override
    public AbstractGithubAdapter onAdapter()
    {
        return new CreditsAdapter(getActivity());
    }
}
