import os
import re
import shutil
import requests
from io import BytesIO
from zipfile import ZipFile
from urllib.request import urlretrieve
from urllib.request import urlopen
from bs4 import BeautifulSoup
import pandas as pd
import numpy as np
import warnings
import luigi
import sys
import logging


def extractZip(url, source_dir, data_dir):
    logging.debug('In the Task : extractZip')
    r = requests.get(url)
    z = ZipFile(BytesIO(r.content))
    z.extractall(source_dir)
    deletePDF(source_dir)
    copyFile(source_dir, data_dir)


def deletePDF(path):
    logging.debug('In the Task : deletePDF')
    for parent, dirnames, filenames in os.walk(source_dir):
        for fn in filenames:
            if fn.lower().endswith('.pdf'):
                print("Deleteting" + fn)
                os.remove(os.path.join(parent, fn))
            if fn.lower().endswith('.doc'):
                print("Deleteting" + fn)
                os.remove(os.path.join(parent, fn))
            if fn.startswith("RPSR"):
                print("Deleteting" + fn)
                os.remove(os.path.join(parent, fn))
            if fn.startswith("INDI"):
                print("Deleteting" + fn)
                os.remove(os.path.join(parent, fn))
            if fn.startswith("THER"):
                print("Deleteting" + fn)
                os.remove(os.path.join(parent, fn))


def copyFile(source_dir, data_dir):
    logging.debug('In the Task : copyFiles')
    RootDir1 = os.getcwd() + '/' + source_dir
    TargetFolder = os.getcwd() + '/' + data_dir
    for root, dirs, files in os.walk((os.path.normpath(RootDir1)), topdown=False):
        for name in files:
            if name.endswith('.txt'):
                SourceFolder = os.path.join(root, name)
                shutil.move(SourceFolder, TargetFolder)

class get_files_url(luigi.Task):

    logging.debug('In the Task : getWebUrls')

    def requires(self):
        return []

    def run(self):
        source_dir = "FAERSsrc"
        data_dir = "FAERSdata"
        files = {}
        url = {}
        host_url = "http://www.fda.gov"
        target_page = [
            "http://www.fda.gov/Drugs/GuidanceComplianceRegulatoryInformation/Surveillance/AdverseDrugEffects/ucm082193.htm"]
        for page_url in target_page:
            try:
                page_bs = BeautifulSoup(urlopen(page_url), "lxml")
            except:
                page_bs = BeautifulSoup(urlopen(page_url))
            for url in page_bs.find_all("a"):
                a_string = str(url.string)
                if "ASCII" in a_string.upper():
                    files[a_string.encode("utf-8")] = host_url + url["href"]
                    url = host_url + url["href"]
                    extractZip(url, source_dir, data_dir)
            for url in page_bs.find_all("linktitle"):
                a_string = str(url.string)
                if "ASCII" in a_string.upper():
                    files[a_string.encode("utf-8")] = host_url + url.parent["href"]
                    url = host_url + url.parent["href"]
                    extractZip(url, source_dir, data_dir)
        with self.output().open('w') as f:
                f.write("hello")

    def output(self):
        return luigi.LocalTarget('url.txt')


class mergeData(luigi.Task):

    def requires(self):
        return [get_files_url()]

    def run(self):

        directoryPath = os.getcwd() + "/FAERSdata"
        demo = pd.DataFrame(
            columns=['primaryid', 'caseid', 'mfr_dt', 'init_fda_dt', 'rept_cod', 'mfr_num', 'mfr_sndr', 'age',
                     'sex', 'wt', 'wt_cod', 'occp_cod', 'occr_country'])
        drug = pd.DataFrame(columns=['primaryid', 'caseid', 'role_cod', 'drugname', 'route', 'dose_amt', 'dose_unit',
                                     'dose_form', 'dose_freq'])
        reaction = pd.DataFrame(columns=['primaryid', 'caseid', 'pt'])
        outcome = pd.DataFrame(columns=['primaryid', 'caseid', 'outc_cod'])
        print("in run")
        for filename in os.listdir(directoryPath):
            if "DEMO" in filename:
                demo_df = pd.read_csv(directoryPath + "/" + filename, low_memory=False, sep="$", error_bad_lines=False)
                demo_df.drop(
                    ['caseversion', 'i_f_code', 'lit_ref', 'event_dt', 'auth_num', 'fda_dt', 'age_cod', 'age_grp',
                     'e_sub', 'rept_dt', 'to_mfr', 'reporter_country'], inplace=True, axis=1, errors='ignore')
                demo_df = demo_df.loc[(demo_df['wt_cod'] == 'KG')]
                demo_df = demo_df[pd.notnull(demo_df['age'])]
                demo_df = demo_df[1:]
                demo = demo.append(demo_df, ignore_index=True)
            if "DRUG" in filename:
                durg_df = pd.read_csv(directoryPath + "/" + filename, low_memory=False, sep="$", error_bad_lines=False)
                durg_df.drop(['drug_seq', 'val_vbm', 'dose_vbm', 'cum_dose_chr', 'prod_ai', 'cum_dose_unit', 'dechal',
                              'rechal', 'lot_num', 'exp_dt', 'nda_num'], inplace=True, axis=1, errors='ignore')
                durg_df = durg_df[pd.notnull(durg_df['dose_amt'])]
                durg_df = durg_df[pd.notnull(durg_df['dose_unit'])]
                durg_df = durg_df.loc[(durg_df['role_cod'] == 'PS')]
                durg_df = durg_df[1:]
                drug = drug.append(durg_df, ignore_index=True)
            if "REAC" in filename:
                reac_df = pd.read_csv(directoryPath + "/" + filename, low_memory=False, sep="$", error_bad_lines=False)
                reac_df = reac_df.groupby('primaryid')
                reac_df = reac_df.filter(lambda x: len(x) == 1)
                reac_df = reac_df[1:]
                reaction = reaction.append(reac_df, ignore_index=True)
            if "OUTC" in filename:
                out_df = pd.read_csv(directoryPath + "/" + filename, low_memory=False, sep="$", error_bad_lines=False)
                out_df = out_df.groupby('primaryid')
                out_df = out_df.filter(lambda x: len(x) == 1)
                out_df = out_df[1:]
                outcome = outcome.append(out_df, ignore_index=True)

        demo['sex'] = np.where(pd.isnull(demo['sex']), demo['gndr_cod'], demo['sex'])
        demo.drop(['gndr_cod'], inplace=True, axis=1, errors='ignore')
        demo_durg_df = pd.merge(drug, demo, on=('primaryid', 'caseid'), how='left')
        demodurgreact_df = pd.merge(demo_durg_df, reaction, on=('primaryid', 'caseid'), how='inner')
        demodrugreactout_df = pd.merge(demodurgreact_df, outcome, on=('primaryid', 'caseid'), how='inner')
        demodrugreactout_df.drop(['drug_rec_act'], inplace=True, axis=1, errors='ignore')
        demodrugreactout_df['occp_cod'] = demodrugreactout_df['occp_cod'].fillna('OT')
        demodrugreactout_df['rept_cod'] = demodrugreactout_df['rept_cod'].fillna('EXP')
        demodrugreactout_df['mfr_sndr'] = demodrugreactout_df['mfr_sndr'].fillna('Others')
        demodrugreactout_df['route'] = demodrugreactout_df['route'].fillna('Unknown')
        demodrugreactout_df['dose_form'] = demodrugreactout_df['dose_form'].fillna('Others')
        demodrugreactout_df['dose_freq'] = demodrugreactout_df['dose_freq'].fillna('Others')
        demodrugreactout_df.to_csv(self.output().path, header=True, index=False);

    def output(self):
        return luigi.LocalTarget('MergedFile.csv')

if __name__ == '__main__':
    source_dir = "FAERSsrc"
    data_dir = "FAERSdata"
    if not os.path.isdir(source_dir):
        os.makedirs(source_dir)
    if not os.path.isdir(data_dir):
        os.makedirs(data_dir)
    luigi.run()
